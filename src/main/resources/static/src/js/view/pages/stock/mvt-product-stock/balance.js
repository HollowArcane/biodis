class BalanceSection extends SimplePage
{
    static ALL = {id: '', label: 'Tous'};
    constructor(form, table, categories, subcategories)
    {
        super();
        this.form = FormHandler.for(form);
        this.table = table;
        this.categories = categories;
        this.subcategories = subcategories;
    }

    init()
    {
        this.categories.onchange = e => {
            const value = parseInt(this.categories.value || 0);
            // only filter if value != 0
            const filter = value === 0 ? this.data.subcategories:
                this.data.subcategories.filter(item => item.idProductCategory === value || item.id === '');
            this.subcategories.replaceChildren(...Options(filter, 'id', 'label'));
            this.subcategories.value = filter[0].id;
        };

        this.form.onsubmit(this.read.bind(this));
        this.read();
    }

    read()
    {
        fetch(`/stock/mvt-product-stock/balance?${this.form.queryParams([
            'ndays', 'date', 'idCategory', 'idSubcategory'
        ])}`)
        .then(async (response) => {
            if(response.status === 200)
            { this.render((await response.json()).data); }
            else
            { this.error(response); }
        })
        .catch(this.error.bind(this));
    }

    static createHeaderSpan(name, group, bgColor, fgColor)
    { return tag('th',
        {
            'class': 'border',
            'colspan': (group.nestedSize || 1)*3,
            'style': `color: ${fgColor}; background: ${bgColor}`
        },
        [ text(name) ]
    ); }

    render(data)
    {
        this.data = data;
        this.data.categories.unshift(BalanceSection.ALL);
        this.data.subcategories.unshift(BalanceSection.ALL);

        this.categories.replaceChildren(...Options(this.data.categories, 'id', 'label'));
        this.subcategories.replaceChildren(...Options(this.data.subcategories, 'id', 'label'));


        const colors1 = [
            '#3F51B5', '#FF8F00', '#1976D2', '#558B2F', '#0097A7', '#5D4037', '#00897B'
        ];

        const colors2 = [
            '#F6F6F6', '#FFFFFF'
        ]

        const colors3 = [
            '#FFEBEE', '#EDE7F6', '#E3F2FD', '#E0F2F1', '#FFF3E0', '#EFEBE9', '##CEFF1', '#FCE4EC'
        ]

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
            [tag('th', {}, [text('Date de Réference')])], // entry, withdrawal, balance
        ]

        const tableBody = [];

        const categories = data.data[data.dates[0]].items;
        for(let categoryName in categories)
        {
            tableHeaders[0].push(BalanceSection.createHeaderSpan(categoryName, categories[categoryName], colors1[tableHeaders[0].length % colors1.length], 'white'));
            const subcategories = categories[categoryName].items;
            for(let subcategoryName in subcategories)
            {
                tableHeaders[1].push(BalanceSection.createHeaderSpan(subcategoryName, subcategories[subcategoryName], colors2[tableHeaders[1].length % colors2.length], 'black'));
                const products = subcategories[subcategoryName].items;
                for(let productName in products)
                {
                    tableHeaders[2].push(BalanceSection.createHeaderSpan(productName, products[productName], colors3[tableHeaders[2].length % colors3.length]));
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
}
