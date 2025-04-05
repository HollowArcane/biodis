
function ProgressBar(progression, min, max, danger)
{
    return tag('div', {'class':`progress d-flex ${progression < 0 ? 'justify-content-end': 'justify-content-start'}`, style: 'height: 20px'}, [
        tag('div', {
            'class': `progress-bar progress-bar-striped progress-bar-animated ${danger ? 'bg-danger': ''}`,
            'data-mdb-tooltip-init': true,
            'title': progression,
            'role': 'progressbar',
            'aria-valuenow': progression,
            'aria-valuemin': min,
            'aria-valuemax': max,
            'style': `width: ${100 * Math.abs(progression - min) / (max - min)}%;`
        }, [text(progression)])
    ])
}