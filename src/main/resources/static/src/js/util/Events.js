/* SelectFilter.for(this.categories)
    .filter(data, (value, item) => item.idProductCategory === value || item.id === '')
    .filterByField(this.data.subcategories, 'idProductCategory')
    
    .then(filter => this.subcategories.replaceChildren(...Option))
    .renderOptions('id', 'label')
    .applyOn(this.subcategories) */

// class Events
// {
//     constructor(event)
//     { this.event = event; }

//     static filter(data, callback)
//     {
//         new Events(value => {
//             return value === '' ? data: data.filter(item => callback(value, item));
//         });
//     }

//     then(callback)
//     { this.after = callback; }

//     on(event, item)
//     {
//         item.addEventListener(event, e => {
//             const result = this.event(item.value);
//             if(this.after)
//             { this.after(result); }
//         })
//     }
// }

// function filter(selector, value, items, filterFunction, renderFunction)
// {
//     const filter = value === '' ? items: items.filter(filterFunction.bind(value))
//     selector.replaceChildren(...renderFunction(filter));
//     selector.value = '';
// }

/**
 * function filter(selector, value, items, filterFunction, renderFunction)
 * {
 *  const filter = value === '' ? items: items.filter(filterFunction.bind(value))
 *  selector.replaceChildren(..renderFunction(filter));
 *  selector.value = '';
 * }
 * 
 * parent.onselect = e => filter(child, e.target.value);
 */