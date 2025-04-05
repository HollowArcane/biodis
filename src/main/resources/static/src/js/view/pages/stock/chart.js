const ALL = {id: '', label: 'Tous'};
const MONTHS = ['Jan', 'Fév', 'Mars', 'Avr', 'Mai', 'Juin', 'Juil', 'Août', 'Sept', 'Oct', 'Nov', 'Déc'];
const TIME_UNITS = {month: 'Mois', week: 'Semaine', day: 'Jour'};

class Page extends SimplePage
{
    constructor(btnPdf, form, chart, sampleType, categories, subcategories)
    {
        super();
        this.btnPdf = btnPdf;
        this.form = form;
        this.chart = chart;
        this.sampleType = sampleType;
        this.categories = categories;
        this.subcategories = subcategories;
    }

    init()
    {
        this.btnPdf.onclick = this.exportPDF.bind(this);

           
        this.categories.onchange = e => {
            const value = parseInt(this.categories.value || 0);
            // only filter if value != 0
            const filter = value === 0 ? this.data.subcategories:
                this.data.subcategories.filter(item => item.idProductCategory === value || item.id === '');
            this.subcategories.replaceChildren(...Options(filter, 'id', 'label'));
            this.subcategories.value = filter[0].id;
        };


        this.form.onsubmit = e => {
            e.preventDefault();
            this.read();
        }
        this.read();
    }

    read()
    {
        const formdata = new FormData(this.form);
        fetch(`/stock/chart?${toQuery(formdata, [
            'accumulate', 'date', 'nSamples', 'idCategory', 'sampleType', 'idSubcategory', 'group'
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
        const formdata = new FormData(this.form);
        const nSamples = parseInt(formdata.get('nSamples'));
        let timeUnit = TIME_UNITS[formdata.get('sampleType')];
        if(nSamples > 1 && !timeUnit.endsWith('s'))
        { timeUnit += 's' }

        return `Graphique du Bilan des ${nSamples} derniers ${timeUnit}`;
    }

    exportPDF()
    {
        this.chart.toBlob(blob => {
            const formData = new FormData();
            formData.append("image", blob, "canvas-image.png");
        
            fetch("/image/upload", {
                method: "POST",
                body: formData,
            })
            .then(async (response) => {
                if(response.status === 201)
                {
                    const json = await response.json();
                    openTab(`/image/pdf?name=graphique&file=${json.data.filename}&title=${this.generateTitle()}`);
                }
                else
                { this.error(response); }
            })
            .catch(this.error.bind(this));
        }, "image/png");
    }

    formatDate(date)
    {
        const split = date.split('-');
        const year = parseInt(split[0]);
        if(this.sampleType.value === 'month')
        {
            const month = MONTHS[
                (parseInt(split[1]) - 2 + MONTHS.length) %
                MONTHS.length
            ];

            return `${month} ${month === 'Déc' ? year - 1: year}`;
        }

        const month = MONTHS[parseInt(split[1]) - 1];
        return `${split[2]} ${month} ${split[0]}`;
    }

    render(data)
    {
        this.data = data;
        this.data.categories.unshift(ALL);
        this.data.subcategories.unshift(ALL);

        this.categories.replaceChildren(...Options(this.data.categories, 'id', 'label'));
        this.subcategories.replaceChildren(...Options(this.data.subcategories, 'id', 'label'));


        const formdata = new FormData(this.form);
        const products = Object.keys(data.data);
        if(products.length === 0)
        {
            alert('Aucune donnée trouvée');
            return;
        }

        if(this.chartModel)
        { this.chartModel.destroy(); }
        this.chartModel = new Chart(this.chart, {
            type: formdata.get('accumulate') === 'accumulate' ? 'line': 'bar',
            data: {
                labels: data.dates.map(this.formatDate.bind(this)),
                datasets: products.map(p => { return {
                    label: p,
                    data: data.dates.map(date => data.data[p][date]),
                }})
            },
            options: {
                responsive: true,
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        });
    }
}

window.addEventListener('load', e => {
    new Page(
        document.getElementById('btn-pdf'),
        document.getElementById('form'),
        document.getElementById('chart'),
        document.getElementById('sampleType'),
        document.getElementById('idCategory'),
        document.getElementById('idSubcategory'),
    ).init()
})