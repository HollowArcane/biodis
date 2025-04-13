const heading = document.querySelector('#modal-title');

class Page extends CRUDPage
{
    constructor(btnAdd, table, form)
    { super('/sale/customer', btnAdd, table, form); }

    init()
    {
        super.init();
        
        this.btnAdd.onclick = () => {
            this.form.reset();
            heading.textContent = 'Insertion Client';
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
                tag('td', {}, [tag('div', {'class': 'd-flex gap-1'}, [
                    BtnEdit(() => {
                        heading.textContent = 'Modification Client';
                        const {firstName, lastName, phoneNumber, email} = row;
                        this.form.load({firstName, lastName, phoneNumber, email});
                        this.form.onsubmit(this.update.bind(this, row.id));
                    }),
                    BtnDelete(this.delete.bind(this, row.id))    
                ])]),
                tag('td', {}, [text(row.firstName)]),
                tag('td', {}, [text(row.lastName)]),
                tag('td', {align: 'right'}, [text(row.phoneNumber || '')]),
                tag('td', {}, [text(row.email || '')])
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