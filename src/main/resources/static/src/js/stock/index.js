const heading = document.querySelector('#modal-title');

class Page extends CRUDPage
{
    constructor(btnAdd, table, form)
    { super('/stock', btnAdd, table, form); }

    init()
    {
        super.init();
        this.btnAdd.onclick = () => {
            this.form.reset();
            heading.textContent = 'Insertion Mouvement de Stock';
            this.form.onsubmit(this.create.bind(this))
        };
    }

    render(data)
    {
        const tbody = table.querySelector('tbody');
        tbody.replaceChildren();

        for(const row of data.content)
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
                                heading.textContent = 'Modification Mouvement de Stock';
                                this.form.load({
                                    'idProduct': row.idProduct,
                                    'action': row.idAction,
                                    'quantity': row.absoluteQuantity,
                                    'date': row.date
                                });
                                this.form.onsubmit(this.update.bind(this, row.id));
                            }
                        },
                        [icon({}, ['fa', 'fa-pencil'])]
                    ),
                    tag('button', {'class': 'btn btn-secondary text-danger','onclick': this.delete.bind(this, row.id)}, [icon({}, ['fa', 'fa-trash'])]),
                ]),
                tag('td', {}, [text(row.productCategory)]),
                tag('td', {}, [text(row.productSubcategory)]),
                tag('td', {}, [text(row.product)]),
                tag('td', {}, [text(row.action)]),
                tag('td', {'align': 'right'}, [text(row.absoluteQuantity)]),
                tag('td', {}, [text(row.date)]),
            ]));
        }
        
        const pagination = new Pagination(
            document.querySelector('.pagination'),
            tag('li', {'class': 'page-item'}, [
                tag('a', {'href': '#', 'class': 'slot page-link'}, [])
            ]),
            data.totalPages,
            this.read.bind(this)
        );
        pagination.init();
        pagination.setActive(data.number + 1);
    }
}

window.addEventListener('load', e => {
    new Page(
        document.getElementById('btn-add'),
        document.getElementById('table'),
        document.getElementById('form')
    ).init()
})