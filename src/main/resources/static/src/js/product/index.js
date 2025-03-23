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
                    BtnEdit(() => {
                        heading.textContent = 'Modification Produit';
                        const {label, idProductSubcategory} = row;
                        this.form.load({label, idProductSubcategory});
                        this.form.onsubmit(this.update.bind(this, row.id));
                    }),
                    BtnDelete(this.delete.bind(this, row.id))
                ]),
                tag('td', {}, [text(row.label)]),
                tag('td', {}, [text(row.productSubcategory)]),
                tag('td', {}, [text(row.productCategory)]),
                tag('td', {align: 'right'}, [text(row.thresholdWarning)]),
                tag('td', {}, [ProgressBar(
                    row.signedBalance,
                    0,
                    Math.max(1.2 * row.signedBalance, 3 * row.thresholdWarning),
                    row.signedBalance <= row.thresholdWarning
                )])
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