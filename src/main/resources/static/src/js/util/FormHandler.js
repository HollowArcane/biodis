
class FormHandler
{
    constructor(form)
    {
        if (!(form instanceof HTMLFormElement))
        { throw new Error('Provided element must be a <form> element.'); }
        this._form = form;
    }

    static for(form)
    { return new FormHandler(form); }

    // please always specify params to inmprove readability and control
    queryParams(params)
    {
        const formData = this.formData();
        let query = '';
        // params = params || formData.keys();

        for(const key of params)
        {
            if(formData.get(key) !== null)
            { query += `${key}=${formData.get(key)}&`; }
        } 

        return query.substring(0, query.length - 1);
    }

    formData()
    { return new FormData(this._form); }

    onsubmit(callback)
    {
        this._form.onsubmit = e => {
            e.preventDefault();
            callback();
        }
    }

    triggerOnsubmit()
    { this._form.dispatchEvent(new Event('submit')); }

    load(data)
    {
        this.reset()
        for(let key in data)
        {
            const inputs = this._form.querySelectorAll(`input[name="${key}"], select[name="${key}"]`);
            for(const input of inputs)
            {
                input.value = data[key];
            }
        }
    }

    reset()
    {
        this._form.reset();
        this.resetErrors();
    }

    resetErrors()
    {
        const inputs = this._form.querySelectorAll('input, select');
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
            const inputs = this._form.querySelectorAll(`input[name="${key}"], select[name="${key}"]`);
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
