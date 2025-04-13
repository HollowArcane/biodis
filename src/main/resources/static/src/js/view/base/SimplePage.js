class SimplePage
{
    error(error)
    {
        try
        {
            error.json()
            .then(json => {
                if(json.error && json.error.message)
                { Swal.fire({
                    title: 'Erreur',
                    text: json.error.message,
                    icon: 'error'
                }); }
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
}