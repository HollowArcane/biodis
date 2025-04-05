
class FormHandler
{
    constructor(form)
    {
        this.form = form;
    }

    static for(form)
    { return new FormHandler(form); }

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
