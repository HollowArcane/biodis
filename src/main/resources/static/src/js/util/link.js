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