class CRUDPage
{
    constructor(apiPath, btnAdd, table, form)
    {
        this.apiPath = apiPath;
        this.btnAdd = btnAdd;
        this.table = table;
        this.form = new FormHandler(form);
    }

    init()
    {
        this.btnAdd.onclick = () => {
            this.form.reset();
            this.form.onsubmit(this.create.bind(this))
        };
        this.read(1);
    }

    read(page)
    {
        fetch(`${this.apiPath}?page=${page}`)
        .then(async (response) => {
            if(response.status === 200)
            { this.render((await response.json()).data); }
            else
            { alert('Une erreur est survenue. Veuillez réessayer ultérieurement'); }
        })
        .catch(this.error.bind(this));
    }

    error(error)
    {
        try
        {
            error.json()
            .then(json => {
                if(json.error && json.error.message)
                { alert(json.error.message); }

                if(json.error && json.error.details)
                { this.form.displayErrors(json.error.details); }
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

    create()
    {
        fetch(this.apiPath, {
            method: 'POST',
            body: this.form.formData()
        }).then(response => {
            if(response.ok)
            { location.reload(); }
            else
            { this.error(response); }
        });
    }

    update(id)
    {
        fetch(`${this.apiPath}/${id}`, {
            method: 'PUT',
            body: this.form.formData()
        }).then(response => {
            if(response.ok)
            { location.reload(); }
            else
            { this.error(response); }
        });
    }

    delete(id)
    {
        const result = confirm('Êtes-vous sûr de vouloir supprimer cet élément?');

        if(result === true)
        {
            fetch(`${this.apiPath}/${id}`, {
                method: 'DELETE'
            }).then(response => {
                if(response.ok)
                { location.reload(); }
                else
                { this.error(response); }
            });
        }
    }
}