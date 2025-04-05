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

