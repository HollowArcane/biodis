class ChartSection extends SimplePage
{
    static MONTHS = ['Jan', 'Fév', 'Mars', 'Avr', 'Mai', 'Juin', 'Juil', 'Août', 'Sept', 'Oct', 'Nov', 'Déc'];
    static TIME_UNITS = {month: 'Mois', week: 'Semaine', day: 'Jour'};
    static OPTION_PARAMS = {value: 'id', label: 'label'};
    static ALL = {id: '', label: 'Tous'};

    constructor(formFilter, formConfig, chart, categories, subcategories, message)
    {
        super();
        this.formFilter = FormHandler.for(formFilter);
        this.formConfig = FormHandler.for(formConfig);
        this.chart = chart;
        this.categories = SelectWithFilter.for(categories, [], ChartSection.OPTION_PARAMS);
        this.subcategories = SelectWithFilter.for(subcategories, [], ChartSection.OPTION_PARAMS);
        this.message = message;
    }

    static formatDate(sampleType, date)
    {   
        const split = date.split('-');
        const year = parseInt(split[0]);
        if(sampleType === 'month')
        {
            const month = ChartSection.MONTHS[
                (parseInt(split[1]) - 2 + ChartSection.MONTHS.length) % ChartSection.MONTHS.length
            ];
    
            return `${month} ${month === 'Déc' ? year - 1: year}`;
        }
        if(sampleType === 'year')
        { return `${year}`; }
    
        const month = ChartSection.MONTHS[parseInt(split[1]) - 1];
        return `${split[2]} ${month} ${split[0]}`;
    }

    init()
    {
        this.categories.onchange(() => {
            const value = parseInt(this.categories.value || 0);
            // only filter if value != 0
            if(value !== 0)
            { this.subcategories.filter(item => item.idProductCategory === value || item.id === ''); }
            else
            { this.subcategories.reset(); }
        });


        this.formFilter.onsubmit(this.read.bind(this));
        this.formConfig.onsubmit(this.read.bind(this));

        this.read();
    }

    read()
    {
        fetch(`/stock/mvt-product-stock/chart?${this.formFilter.queryParams([
            'chartIdCategory', 'chartIdSubcategory', 'chartGroup'
        ])}&${this.formConfig.queryParams([
            'chartAccumulate', 'chartDate', 'chartNSamples', 'chartSampleType'
        ])}`)
        .then(async (response) => {
            if(response.status === 200)
            { this.render((await response.json()).data); }
            else
            { this.error(response); }
        })
        .catch(this.error.bind(this));
    }

    generateTitle()
    {
        const formdata = this.formConfig.formData();
        const nSamples = parseInt(formdata.get('chartNSamples'));
        const date = parseInt(formdata.get('chartDate'));
        let timeUnit = ChartSection.TIME_UNITS[formdata.get('chartSampleType')];
        if(nSamples > 1 && !timeUnit.endsWith('s'))
        {
            timeUnit += 's';
            return `Graphique du Bilan des ${nSamples} derniers ${timeUnit} à partir du ${date}`;
        }
        else
        { return `Bilan du dernier ${timeUnit} à partir du ${date}`; }
    }

    exportPDF(then)
    {
        this.chart.toBlob(blob => {
            const formData = new FormData();
            formData.append("image", blob, "canvas-image.png");
        
            fetch("/misc/image/upload", {
                method: "POST",
                body: formData,
            })
            .then(async (response) => {
                if(response.status === 201)
                {
                    const json = await response.json();
                    then(json.data.filename);
                    // openTab(`/misc/image/pdf?name=graphique&file=${json.data.filename}&title=${this.generateTitle()}`);
                }
                else
                { this.error(response); }
            })
            .catch(this.error.bind(this));
        }, "image/png");
    }

    render(data)
    {
        // this.data = data;
        data.categories.unshift(ChartSection.ALL);
        data.subcategories.unshift(ChartSection.ALL);

        this.categories.update(data.categories, ChartSection.OPTION_PARAMS);
        this.subcategories.update(data.subcategories, ChartSection.OPTION_PARAMS);

        const formdata = this.formConfig.formData();
        const products = Object.keys(data.data);

        if(this.chartModel)
        { this.chartModel.destroy(); }

        if(products.length === 0)
        {
            this.message.textContent = 'Aucune donnée trouvée';
            return;
        }
        else
        { this.message.textContent = ''; }

        this.chartModel = new Chart(this.chart, {
            type: formdata.get('chartAccumulate') === 'chartAccumulate' ? 'line': 'bar',
            data: {
                labels: data.dates.map(date => ChartSection.formatDate(formdata.get('chartSampleType'), date)),
                datasets: products.map(p => { return {
                    label: p,
                    data: data.dates.map(date => data.data[p][date]),
                }})
            },
            options: {
                indexAxis: formdata.get('chartDisposition'),
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        });
    }
}
