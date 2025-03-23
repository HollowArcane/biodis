class PfNavbarAdapter
{
    static setup()
    {
        const collapseds = document.querySelectorAll('.pf__navbar li:has(li.active)')
        for(const collapsed of collapseds)
        {
            collapsed.classList.add('active', 'static');
        }

        const collapsibles = document.querySelectorAll('.pf__navbar li:has(> div) .pf__navbar-item')
        for(const collapsible of collapsibles)
        {
            collapsible.onclick = e => {
                collapsible.parentNode.classList.remove('static');
                collapsible.parentNode.classList.toggle('active');
            }
        }
    }
}

function openTab(link)
{
    const tempLink = tag('a', {
        'href': link,
        'target': '_blank',
        'rel': 'noopener noreferer'
    }, []);
    document.body.append(tempLink);
    tempLink.click();
    tempLink.remove();
    
}

window.addEventListener('load', e => PfNavbarAdapter.setup());