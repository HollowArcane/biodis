const heading = document.querySelector('#modal-title');

class Page extends CRUDPage
{
    constructor(btnAdd, table, form)
    { super('/product', btnAdd, table, form); }

    init()
    {
        super.init();
        
        this.btnAdd.onclick = () => {
            this.form.reset();
            heading.textContent = 'Insertion Produit';
            this.form.onsubmit(this.create.bind(this))
        };
    }

    render(data)
    {
        const tbody = table.querySelector('tbody');
        tbody.replaceChildren();

        for(const row of data.page.content)
        {
            tbody.append(tag('tr', {}, [
                tag('td', {'class': 'd-flex gap-1'}, [
                    tag('button',
                        {
                            'class': 'btn btn-secondary text-info',
                            'data-mdb-ripple-init': true,
                            'data-mdb-modal-init': true,
                            'data-mdb-target': '#modal',
                            'onclick': () => {
                                heading.textContent = 'Modification Produit';
                                this.form.load({
                                    'label': row.label,
                                    'idProductSubcategory': row.idProductSubcategory,
                                });
                                this.form.onsubmit(this.update.bind(this, row.id));
                            }
                        },
                        [icon({}, ['fa', 'fa-pencil'])]
                    ),
                    tag('button',
                        {
                            'class': 'btn btn-secondary text-danger',
                            'data-mdb-ripple-init': true,
                            'data-mdb-modal-init': true,
                            'onclick': this.delete.bind(this, row.id)
                        },
                        [icon({}, ['fa', 'fa-trash'])]),
                ]),
                tag('td', {}, [text(row.label)]),
                tag('td', {}, [text(data.subcategories[row.idProductSubcategory].label)]),
                tag('td', {}, [text(data.categories[data.subcategories[row.idProductSubcategory].idProductCategory].label)])
            ]));
        }
        
        const pagination = new Pagination(
            document.querySelector('.pagination'),
            tag('li', {'class': 'page-item'}, [
                tag('a', {'href': '#', 'class': 'slot page-link'}, [])
            ]),
            data.page.totalPages,
            this.read.bind(this)
        );
        pagination.init();
        pagination.setActive(data.page.number + 1);
    }
}

window.addEventListener('load', e => {
    new Page(
        document.getElementById('btn-add'),
        document.getElementById('table'),
        document.getElementById('form')
    ).init();
})