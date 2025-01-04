function tag(tag, attributes, children)
{
    const element = document.createElement(tag);
    for(let key in attributes)
    {
        if(key.startsWith('on'))
        { element.addEventListener(key.substring(2), attributes[key]); }

        else if(attributes[key] === true)
        { element.setAttribute(key, true); }

        else if(attributes[key] !== false)
        { element.setAttribute(key, attributes[key]); }
    }
    for(const child of children)
    {
        element.appendChild(child);
    }
    return element;
}

function text(content)
{
    return document.createTextNode(content);
}

function icon(attribute, names)
{
    const icon = tag('i', attribute, []);
    for(const name of names)
    { icon.classList.add(name); }
    return icon;
}

class FormHandler
{
    constructor(form)
    {
        this.form = form;
    }

    formData()
    { return new FormData(form); }

    onsubmit(callback)
    {
        this.form.onsubmit = e => {
            e.preventDefault();
            callback();
        }
    }

    load(data)
    {
        this.reset()
        for(let key in data)
        {
            const inputs = this.form.querySelectorAll(`input[name="${key}"], select[name="${key}"]`);
            for(const input of inputs)
            {
                input.value = data[key];
            }
        }
    }

    reset()
    {
        this.form.reset();
        this.resetErrors();
    }

    resetErrors()
    {
        const inputs = this.form.querySelectorAll('input, select');
        for(const input of inputs)
        {
            input.classList.remove('is-invalid');
            input.classList.remove('is-valid');
            
            const feedbackWrap = input.nextElementSibling;
            if (feedbackWrap && feedbackWrap.classList.contains('invalid-feedback'))
            {
                feedbackWrap.textContent = '';
            }
        }
    }

    displayErrors( errors)
    {
        this.resetErrors();
        for(let key in errors)
        {
            const inputs = this.form.querySelectorAll(`input[name="${key}"], select[name="${key}"]`);
            for(const input of inputs)
            {
                input.classList.add('is-invalid');     
                const feedbackWrap = input.nextElementSibling;

                if (feedbackWrap && feedbackWrap.classList.contains('invalid-feedback'))
                {
                    feedbackWrap.textContent = errors[key][0];
                }
            }
        }
    }
}
