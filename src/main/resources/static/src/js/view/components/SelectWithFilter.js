/**
 * SelectWithFilter class
 * This class creates a <select> element with options that can be filtered based on a provided filter function.
 * It also provides a method to update the data and re-render the options.
 * 
 */
class SelectWithFilter
{
    constructor(selectElement, data, optionParams)
    {
        if (!(selectElement instanceof HTMLSelectElement))
        { throw new Error('Provided element must be a <select> element.'); }
        if (!Array.isArray(data))
        { throw new Error('Data must be an array.'); }
        if(!optionParams.value || !optionParams.label)
        { throw new Error('Options must contain value and label properties.'); }

        this._selectElement = selectElement;
        this._data = data;
        this._activeData = data;
        this._optionParams = optionParams

        this.render();
    }

    static for(element, data, optionParams)
    { return new SelectWithFilter(element, data, optionParams); }

    value()
    { return this._selectElement.value; }

    update(data, optionParams)
    {
        this._data = data;
        this._optionParams = optionParams;
        this.reset();
    }

    triggerOnchange(e)
    { this._selectElement.dispatchEvent(new Event('change')); }

    onchange(event)
    {
        if (typeof event === 'function')
        { this._selectElement.addEventListener('change', event) ; }
        else
        { throw new Error('event is not a function.'); }
    }

    reset()
    {
        this._activeData = this._data;
        this.render();
    }

    filter(filterFunction)
    {
        if (typeof filterFunction !== 'function')
        { throw new Error('Filter function must be a function.'); }

        this._activeData = this._data.filter(filterFunction);
        this.render();
    }

    // updateData(newData, optionParams)
    // {
    //     if (!Array.isArray(newData))
    //     { throw new Error('Data must be an array.'); }
    //     if(!optionParams.value || !optionParams.label)
    //     { throw new Error('optionParams must contain value and label properties.'); }    

    //     this.data = newData;
    //     this.render();
    // }

    // Render the optionParams in the <select> element
    render()
    {
        this._selectElement.replaceChildren(
            ...Options(this._activeData, this._optionParams.value, this._optionParams.label)
        );
        if(this._activeData.length > 0)
        { this._selectElement.value = this._activeData[0][this._optionParams.value]; }
        else
        { this._selectElement.value = ''; }
    }
}