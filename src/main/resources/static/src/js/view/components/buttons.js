function BtnEdit(callback)
{
    return tag('button',
        {
            'class': 'btn btn-secondary text-info',
            'data-mdb-ripple-init': true,
            'data-mdb-modal-init': true,
            'data-mdb-target': '#modal',
            'data-mdb-tooltip-init': true,
            'title': 'Modifier',
            'onclick': callback
        },
        [icon({}, ['fa', 'fa-pencil'])]
    );
}

function BtnDelete(callback)
{
    return tag('button',
        {
            'class': 'btn btn-secondary text-danger',
            'data-mdb-ripple-init': true,
            'data-mdb-modal-init': true,
            'data-mdb-tooltip-init': true,
            'title': 'Supprimer',
            'onclick': callback
        },
        [icon({}, ['fa', 'fa-trash'])]
    );
}