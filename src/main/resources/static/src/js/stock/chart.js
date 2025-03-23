const MONTHS = ['Jan', 'Fév', 'Mars', 'Avr', 'Mai', 'Juin', 'Juil', 'Août', 'Sept', 'Oct', 'Nov', 'Déc'];
const TIME_UNITS = {month: 'Mois', week: 'Semaine', day: 'Jour'};

class Page
{
    constructor(btnPdf, form, chart, sampleType)
    {
        this.btnPdf = btnPdf;
        this.form = form;
        this.chart = chart;
        this.sampleType = sampleType;
    }

    init()
    {
        this.btnPdf.onclick = this.exportPDF.bind(this);

        this.form.onsubmit = e => {
            e.preventDefault();
            this.read();
        }
        this.read();
    }

    read()
    {
        const formdata = new FormData(this.form);
        fetch(`/stock/chart?accumulate=${formdata.get('accumulate')}&date=${formdata.get('date')}&nSamples=${formdata.get('nSamples')}&idCategory=${formdata.get('idCategory')}&sampleType=${formdata.get('sampleType')}`)
        .then(async (response) => {
            if(response.status === 200)
            { this.render((await response.json()).data); }
            else
            { alert('Une erreur est survenue. Veuillez réessayer ultérieurement'); }
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
                { alert('Une erreur est survenue. Veuillez réessayer ultérieurement'); }
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
            type: formdata.get("chartType"),
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

    error(error)
    {
        try
        {
            error.json()
            .then(json => {
                if(json.error && json.error.message)
                { alert(json.error.message); }
            })  
            .catch(error => {
                alert('Une erreur est survenue. Veuillez réessayer ultérieurement');
                console.error(error);   
            })  
        }
        catch (e)
        {
            alert('Une erreur est survenue. Veuillez réessayer ultérieurement');
            console.error(error);    
        }
    }
}

window.addEventListener('load', e => {
    new Page(
        document.getElementById('btn-pdf'),
        document.getElementById('form'),
        document.getElementById('chart'),
        document.getElementById('sampleType')
    ).init()
})