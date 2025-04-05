class SimplePage
{
    error(error)
    {
        try
        {
            error.json()
            .then(json => {
                if(json.error && json.error.message)
                { Swal.fire('Erreur', json.error.message, 'error'); }
            })  
            .catch(error => {
                Swal.fire('Erreur', 'Une erreur est survenue. Veuillez réessayer ultérieurement', 'error');
                console.error(error);   
            })  
        }
        catch (e)
        {
            Swal.fire('Erreur', 'Une erreur est survenue. Veuillez réessayer ultérieurement', 'error');
            console.error(error);    
        }
    }
}