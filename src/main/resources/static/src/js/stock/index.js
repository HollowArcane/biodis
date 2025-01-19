const heading = document.querySelector('#modal-title');

class Page extends CRUDPage
{
    constructor(btnAdd, table, form)
    {
        super('/stock', btnAdd, table, form);
        this.selectLoaded = false;

        this.products = document.getElementById('idProduct');
        this.categories = document.getElementById('category');
        this.subcategories = document.getElementById('subcategory');

    }

    init()
    {
        super.init();

        this.btnAdd.onclick = () => {
            this.form.reset();
            this.filterSelect();
            heading.textContent = 'Insertion Mouvement de Stock';
            this.form.onsubmit(this.create.bind(this))
        };
    }

    filterSelect()
    {
        let productFilter = Object.values(this.data.products);
        let subcategoryFilter = Object.values(this.data.subcategories);
        let selectedSubcategory = this.subcategories.value;

        if(this.categories.value !== '')
        {
            const selectedId = parseInt(this.categories.value);
            productFilter = productFilter.filter(v => this.data.categories[this.data.subcategories[v.idProductSubcategory].idProductCategory].id === selectedId);
            
            subcategoryFilter = subcategoryFilter.filter(v => this.data.categories[v.idProductCategory].id === selectedId);
        }

        this.subcategories.replaceChildren();
        this.subcategories.append(tag('option', {'value': ''}, [text('Tous')]));
        for(const row of subcategoryFilter)
        { this.subcategories.append(tag('option', {'value': row.id}, [text(row.label)])); }

        
        if(subcategoryFilter.some(v => v.id === parseInt(selectedSubcategory)))
        { this.subcategories.value = selectedSubcategory; }
        else
        { this.subcategories.value = ''; }
        
        if(this.subcategories.value !== '')
        {
            const selectedId = parseInt(this.subcategories.value);
            productFilter = productFilter.filter(v => this.data.subcategories[v.idProductSubcategory].id === selectedId)
        }
        this.products.replaceChildren();
        for(const row of productFilter)
        { this.products.append(tag('option', {'value': row.id}, [text(row.label)])); }
    }

    loadSelect(data)
    {
        this.categories.replaceChildren();
        this.categories.append(tag('option', {'value': ''}, [text('Tous')]));
        for(let id in data.categories)
        { this.categories.append(tag('option', {'value': id}, [text(data.categories[id].label)])); }
        
        this.subcategories.replaceChildren();
        this.subcategories.append(tag('option', {'value': ''}, [text('Tous')]));
        for(let id in data.subcategories)
        { this.subcategories.append(tag('option', {'value': id}, [text(data.subcategories[id].label)])); }
        
        this.products.replaceChildren();
        for(let id in data.products)
        { this.products.append(tag('option', {'value': id}, [text(data.products[id].label)])); }

        this.categories.onchange = this.filterSelect.bind(this);
        this.subcategories.onchange = this.filterSelect.bind(this);

        this.data = data;
        this.selectLoaded = true;
    }

    render(data)
    {
        if(this.selectLoaded === false)
        { this.loadSelect(data); }

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
                            'data-mdb-tooltip-init': true,
                            'title': 'Modifier',
                            'onclick': () => {
                                heading.textContent = 'Modification Mouvement de Stock';
                                this.categories.value = '';
                                this.subcategories.value = '';
                                this.filterSelect();
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
                    tag('button',
                        {
                            'class': 'btn btn-secondary text-danger',
                            'data-mdb-ripple-init': true,
                            'data-mdb-tooltip-init': true,
                            'title': 'Supprimer',
                            'onclick': this.delete.bind(this, row.id)
                        },
                        [icon({}, ['fa', 'fa-trash'])]
                    ),
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
    ).init()
})