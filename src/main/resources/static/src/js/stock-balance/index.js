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
            const tempLink = tag('a', {
                'href': `/stock-balance/pdf?date=${formdata.get('date')}&ndays=${formdata.get('ndays')}&idCategory=${formdata.get('idCategory')}`,
                'target': '_blank',
                'rel': 'noopener noreferer'
            }, []);
            document.body.append(tempLink);
            tempLink.click();
            tempLink.remove();
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
        fetch(`/stock-balance?date=${formdata.get('date')}&ndays=${formdata.get('ndays')}&idCategory=${formdata.get('idCategory')}`)
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
        const colors1 = [
            '#3F51B5', '#FF8F00', '#1976D2', '#558B2F', '#0097A7', '#5D4037', '#00897B'
        ];
        let cursor1 = 0;

        const colors2 = [
            '#F6F6F6', '#FFFFFF'
        ]
        let cursor2 = 0;

        const colors3 = [
            '#FFEBEE', '#EDE7F6', '#E3F2FD', '#E0F2F1', '#FFF3E0', '#EFEBE9', '##CEFF1', '#FCE4EC'
        ]
        let cursor3 = 0;

        const thead = this.table.querySelector('thead');
        thead.replaceChildren();

        thead.append(tag('tr', {}, merge([tag('th', {}, [])], data.categories.map(category => {
            const length = data.subcategories[category.id].reduce(
                (sum, subcategory) => sum + data.products[subcategory.id].length * 3, 0
            );

            cursor1 = (cursor1 + 1) % colors1.length;
            return tag('th', {'class': 'border', 'colspan': length, 'style': `color: white; background-color: ${colors1[cursor1]}`}, [text(category.label)]);
        }))));

        thead.append(tag('tr', {}, merge([tag('th', {}, [])], data.categories.map(category => {
            return data.subcategories[category.id].map(
                subcategory => {
                    cursor2 = (cursor2 + 1) % colors2.length;
                    return tag('th', {'class': 'border', 'colspan': data.products[subcategory.id].length * 3, 'style': `background-color: ${colors2[cursor2]}`}, [text(subcategory.label)])
                }
            );
        }).flat())));

        thead.append(tag('tr', {}, merge([tag('th', {}, [])], data.categories.map(category => {
            return data.subcategories[category.id].map(subcategory => {
                return data.products[subcategory.id].map(
                    product => {
                        cursor3 = (cursor3 + 1) % colors3.length;
                        return tag('th', {'class': 'border', 'colspan': 3, 'style': `background-color: ${colors3[cursor3]}`}, [text(product.label)])
                    }
                )
            });
        }).flat(2))));

        thead.append(tag('tr', {}, merge([tag('th', {}, [text('Date')])], data.categories.map(category => {
            return data.subcategories[category.id].map(subcategory => {
                return data.products[subcategory.id].map(
                    product => [
                        tag('th', {'class': 'border-start'}, [text('Entrée')]),
                        tag('th', {}, [text('Sortie')]),
                        tag('th', {'class': 'border-end'}, [text('Solde')]),
                    ]
                )
            });
        }).flat(3))));

        const tbody = this.table.querySelector('tbody');
        tbody.replaceChildren();
        for(const date in data.records)
        {
            tbody.append(tag('tr', {}, merge([tag('td', {}, [text(date)])], data.categories.map(category => {
                return data.subcategories[category.id].map(subcategory => {
                    return data.products[subcategory.id].map(
                        product => {
                            const record = data.records[date][product.id] || { entry: '0', withdraw: '0', balance: '0' };
                            return [
                                tag('td', {'align': 'right', 'class': 'border-start'}, [text(record.entry)]),
                                tag('td', {'align': 'right'}, [text(record.withdraw)]),
                                tag('td', {'align': 'right', 'class': 'border-end'}, [text(record.balance)]),
                            ]
                        }
                    )
                });
            }).flat(3))));
        }
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