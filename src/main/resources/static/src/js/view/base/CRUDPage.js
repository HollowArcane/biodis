class CRUDPage
{
    constructor(apiPath, btnAdd, table, form)
    {
        this.apiPath = apiPath;
        this.btnAdd = btnAdd;
        this.table = table;
        this.form = FormHandler.for(form);
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
        try
        {
            fetch(`${this.apiPath}?page=${page}`)
            .then(async (response) => {
                if(response.status === 200)
                { this.render((await response.json()).data); }
                else
                { this.error(response); }
            })
            .catch(this.error.bind(this));
        }
        catch (error)
        { this.error(error); }
    }

    error(error)
    {
        try
        {
            error.json()
            .then(json => {
                if(json.error && json.error.message)
                {
                    const message = json.error.message.trim().toLowerCase();
                    if(message === 'invalid data given')
                    { /* do nothing here, errors will be shown directly on inputs */ }
                    else
                    { Swal.fire({
                        title: 'Erreur',
                        text: json.error.message,
                        icon: 'error'
                    }); }   
                }

                if(json.error && json.error.details)
                { this.form.displayErrors(json.error.details); }
                else
                { Swal.fire({
                    title: 'Erreur',
                    text: 'Une erreur est survenue. Veuillez réessayer ultérieurement',
                    icon: 'error'
                }); }  
            })  
            .catch(error => {
                Swal.fire({
                    title: 'Erreur',
                    text: 'Une erreur est survenue. Veuillez réessayer ultérieurement',
                    icon: 'error'
                });
                console.error(error);   
            })  
        }
        catch (e)
        {
            Swal.fire({
                title: 'Erreur',
                text: 'Une erreur est survenue. Veuillez réessayer ultérieurement',
                icon: 'error'
            });
            console.error(error);    
        }
    }

    create()
    {
        try
        {
            fetch(this.apiPath, {
                method: 'POST',
                body: this.form.formData()
            }).then(response => {
                if(response.ok)
                {
                    Swal.fire({
                        title: 'Succès',
                        text: 'Données insérées avec succès',
                        icon: 'success',
                        timer: 2000,
                        showConfirmButton: false
                    });
                    setTimeout(() => location.reload(), 2000);
                }
                else
                { this.error(response); }
            })
            .catch(this.error.bind(this));
        }
        catch (error)
        { this.error(error); }
    }

    update(id)
    {
        try
        {
            fetch(`${this.apiPath}/${id}`, {
                method: 'PUT',
                body: this.form.formData()
            }).then(response => {
                if(response.ok)
                {
                    Swal.fire({
                        title: 'Succès',
                        text: 'Données mises à jour avec succès',
                        icon: 'success',
                        timer: 2000,
                        showConfirmButton: false
                    });
                    setTimeout(() => location.reload(), 2000);
                }
                else
                { this.error(response); }
            })
            .catch(this.error.bind(this));
        }
        catch (error)
        { this.error(error); }
    }

    delete(id)
    {
        const result = confirm('Êtes-vous sûr de vouloir supprimer cet élément?');

        if(result === true)
        {
            try
            {
                fetch(`${this.apiPath}/${id}`, {
                    method: 'DELETE'
                }).then(response => {
                    if(response.ok)
                    {
                        Swal.fire({
                            title: 'Succès',
                            text: 'Données supprimées avec succès',
                            icon: 'success',
                            timer: 2000,
                            showConfirmButton: false
                        });
                        setTimeout(() => location.reload(), 2000);
                    }
                    else
                    { this.error(response); }
                })
            .catch(this.error.bind(this));
            
            }
            catch (error)
            { this.error(error); }
        }
    }
}