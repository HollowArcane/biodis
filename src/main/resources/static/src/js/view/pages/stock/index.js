const ALL = {id: '', label: 'Tous'};
const heading = document.querySelector('#modal-title');

class Page extends CRUDPage
{
    constructor(btnAdd, table, form, categories, subcategories, product)
    {
        super('/stock', btnAdd, table, form);
        this.categories = categories;
        this.subcategories = subcategories;
        this.products = products;
    }

    init()
    {
        super.init();
        this.btnAdd.onclick = () => {
            this.form.reset();
            heading.textContent = 'Insertion Mouvement de Stock';
            this.form.onsubmit(this.create.bind(this))
        };
    }

    createOptions(data)
    {
        this.data = data;
        this.data.categories.unshift(ALL);
        this.data.subcategories.unshift(ALL);

        this.categories.replaceChildren(...Options(this.data.categories, 'id', 'label'));
        this.subcategories.replaceChildren(...Options(this.data.subcategories, 'id', 'label'));
        this.products.replaceChildren(...Options(this.data.products, 'id', 'label'));
        
        this.categories.onchange = e => {
            const value = parseInt(this.categories.value || 0);
            // only filter if value != 0
            const filter = value === 0 ? this.data.subcategories:
                this.data.subcategories.filter(item => item.idProductCategory === value || item.id === '');
            this.subcategories.replaceChildren(...Options(filter, 'id', 'label'));
            this.subcategories.value = filter[0].id;
            this.subcategories.onchange(e);
        };

        this.subcategories.onchange = e => {
            const superValue = parseInt(this.categories.value || 0);
            const value = parseInt(this.subcategories.value || 0);
            // only filter if value != 0
            let filter = this.data.products;
            if(value !== 0)
            { filter = this.data.products.filter(item => item.idProductSubcategory === value); }
            else if(superValue !== 0)
            { filter = this.data.products.filter(item => item.idProductCategory === superValue); }
                
            
            this.products.replaceChildren(...Options(filter, 'id', 'label'));
            this.products.value = filter[0].id;
        };
    }

    render(data)
    {
        this.createOptions(data);
        const tbody = table.querySelector('tbody');
        tbody.replaceChildren();

        for(const row of data.page.content)
        {
            tbody.append(tag('tr', {}, [
                tag('td', {}, [tag('div', {'class': 'd-flex gap-1'}, [
                    BtnEdit(() => {
                        heading.textContent = 'Modification Mouvement de Stock';
                        const {idProduct, quantityIn, quantityOut, date} = row;
                        this.form.load({idProduct, quantityIn, quantityOut, date});
                        this.form.onsubmit(this.update.bind(this, row.id));
                    }),
                    BtnDelete(this.delete.bind(this, row.id))
                ])]),
                tag('td', {}, [text(row.productCategory)]),
                tag('td', {}, [text(row.productSubcategory)]),
                tag('td', {}, [text(row.product)]),
                tag('td', {}, [text(row.action)]),
                tag('td', {align: 'right'}, [text(row.absoluteBalance)]),
                tag('td', {}, [text(row.date)]),
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
        document.getElementById('form'),
        document.getElementById('category'),
        document.getElementById('subcategory'),
        document.getElementById('idProduct')
    ).init()
})