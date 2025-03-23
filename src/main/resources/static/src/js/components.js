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

function Options(items, value, label)
{
    const options = [];
    for(const key in items)
    {
        options.push(tag('option',
            {value: items[key][value]},
            [text(items[key][label])]
        ));
    }
    return options;
}

function ProgressBar(progression, min, max, danger)
{
    return tag('div', {'class':'progress', style: 'height: 20px'}, [
        tag('div', {
            'class': `progress-bar progress-bar-striped progress-bar-animated ${danger ? 'progress-bar-danger': ''}`,
            role: 'progressbar',
            'aria-valuenow': progression,
            'aria-valuemin': min,
            'aria-valuemax': max,
            style: `width: ${100 * (progression - min) / (max - min)}%;`
        }, [text(progression)])
    ])
}
