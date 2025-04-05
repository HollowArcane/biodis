
// please always specify params to inmprove readability and control
function toQuery(formData , params )
{
    let query = '';
    // params = params || formData.keys();

    for(const key of params)
    { query += `${key}=${formData.get(key)}&`; } 

    return query.substring(0, query.length - 1);
}