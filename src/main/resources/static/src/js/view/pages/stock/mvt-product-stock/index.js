const ALL = {id: '', label: 'Tous'};
const OPTION_PARAMS = {value: 'id', label: 'label'};
const heading = document.querySelector('#modal-title');

class Page extends CRUDPage
{
    constructor(btnAdd, table, form, categories, subcategories, products)
    {
        super('/stock/mvt-product-stock', btnAdd, table, form);
        this.categories = SelectWithFilter.for(categories, [], OPTION_PARAMS);
        this.subcategories = SelectWithFilter.for(subcategories, [], OPTION_PARAMS);
        this.products = SelectWithFilter.for(products, [], OPTION_PARAMS);
    }

    init()
    {
        super.init();
        
        this.categories.onchange(e => {
            const value = parseInt(this.categories.value() || 0);
            // only filter if value != 0
            if(value !== 0)
            { this.subcategories.filter(item => item.idProductCategory === value || item.id === '') }
            else
            { this.subcategories.reset() }
            this.subcategories.triggerOnchange(e);
        });

        this.subcategories.onchange(e => {
            const superValue = parseInt(this.categories.value() || 0);
            const value = parseInt(this.subcategories.value() || 0);
            // only filter if value != 0
            if(value !== 0)
            { this.products.filter(item => item.idProductSubcategory === value); }
            else if(superValue !== 0)
            { this.products.filter(item => item.idProductCategory === superValue); }
            else
            { this.products.reset(); }  
        });


        this.btnAdd.onclick = () => {
            this.form.reset();
            heading.textContent = 'Insertion Mouvement de Stock';
            this.form.onsubmit(this.create.bind(this))
        };
    }

    createOptions(data)
    {
        // this.data = data;
        data.categories.unshift(ALL);
        data.subcategories.unshift(ALL);
        
        this.categories.update(data.categories, OPTION_PARAMS);
        this.subcategories.update(data.subcategories, OPTION_PARAMS);
        this.products.update(data.products, OPTION_PARAMS);

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