function createHeaderSpan(name, group)
{ return tag('th',
    {
        'class': 'border',
        'colspan': (group.nestedSize || 1)*3,
        // 'style': `color: ${group.props.color}; background: ${group.props.bgColor}`
    },
    [ text(name) ]
); }

class Page
{
    constructor(btnPdf, form, table)
    {
        this.btnPdf = btnPdf;
        this.form = form;
        this.table = table;
    }

    init()
    {
        this.btnPdf.onclick = e => {
            const formdata = new FormData(this.form);
            openTab(`/stock/pdf?date=${formdata.get('date')}&ndays=${formdata.get('ndays')}&idCategory=${formdata.get('idCategory')}`);
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
        fetch(`/stock/balance?date=${formdata.get('date')}&ndays=${formdata.get('ndays')}&idCategory=${formdata.get('idCategory')}`)
        .then(async (response) => {
            if(response.status === 200)
            { this.render((await response.json()).data); }
            else
            { alert('Une erreur est survenue. Veuillez réessayer ultérieurement'); }
        })
        .catch(this.error.bind(this));
    }

    render(data)
    {
        // const colors1 = [
        //     '#3F51B5', '#FF8F00', '#1976D2', '#558B2F', '#0097A7', '#5D4037', '#00897B'
        // ];
        // let cursor1 = 0;

        // const colors2 = [
        //     '#F6F6F6', '#FFFFFF'
        // ]
        // let cursor2 = 0;

        // const colors3 = [
        //     '#FFEBEE', '#EDE7F6', '#E3F2FD', '#E0F2F1', '#FFF3E0', '#EFEBE9', '##CEFF1', '#FCE4EC'
        // ]
        // let cursor3 = 0;

        if(data.dates.length === 0)
        {
            this.table.replaceChildren(tag('tbody', {}, [
                tag('td', {}, [text('Aucune donnée.')])
            ]));
            return;
        }
        
        const tableHeaders = [
            [tag('th', {}, [])], // categories (add one empty column for date)
            [tag('th', {}, [])], // subcategories
            [tag('th', {}, [])], // products
            [tag('th', {}, [text('Date')])], // entry, withdrawal, balance
        ]

        const tableBody = [];

        const categories = data.data[data.dates[0]].items;
        for(let categoryName in categories)
        {
            tableHeaders[0].push(createHeaderSpan(categoryName, categories[categoryName]));
            const subcategories = categories[categoryName].items;
            for(let subcategoryName in subcategories)
            {
                tableHeaders[1].push(createHeaderSpan(subcategoryName, subcategories[subcategoryName]));
                const products = subcategories[subcategoryName].items;
                for(let productName in products)
                {
                    tableHeaders[2].push(createHeaderSpan(productName, products[productName]));
                    tableHeaders[3].push(
                        tag('th', {'class': 'border-start'}, [text('Entrée')]),
                        tag('th', {}, [text('Sortie')]),
                        tag('th', {'class': 'border-end'}, [text('Solde')]),
                    );
                }
            }
        }

        for(let date in data.data)
        {
            const row = [tag('td', {}, [text(date)])];
            const categories = data.data[date].items;
            for(let categoryName in categories)
            {
                const subcategories = categories[categoryName].items;
                for(let subcategoryName in subcategories)
                {
                    const products = subcategories[subcategoryName].items;
                    for(let productName in products)
                    {
                        row.push(tag('td', {'align': 'right'}, [text(products[productName][0].quantityIn)]))
                        row.push(tag('td', {'align': 'right'}, [text(products[productName][0].quantityOut)]))
                        row.push(tag('td', {'align': 'right'}, [text(products[productName][0].balance)]))
                    }
                }
            }
            tableBody.push(row);
        }

        this.table.replaceChildren(
            tag('thead', {}, tableHeaders.map(row => tag('tr', {}, row))),
            tag('tbody', {}, tableBody.map(row => tag('tr', {}, row)))
        );
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

function merge(arr1, arr2)
{
    const arr = [];
    for(const item of arr1)
    { arr.push(item); }
    for(const item of arr2)
    { arr.push(item); }

    return arr;
}

window.addEventListener('load', e => {
    new Page(
        document.getElementById('btn-pdf'),
        document.getElementById('form'),
        document.getElementById('table')
    ).init()
})