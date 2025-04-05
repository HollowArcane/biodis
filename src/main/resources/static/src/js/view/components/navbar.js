// ATTENTION:
// used to make collapsible navbar
// unsed for now


// class PfNavbarAdapter
// {
//     static setup()
//     {
//         const collapseds = document.querySelectorAll('.pf__navbar li:has(li.active)')
//         for(const collapsed of collapseds)
//         {
//             collapsed.classList.add('active', 'static');
//         }

//         const collapsibles = document.querySelectorAll('.pf__navbar li:has(> div) .pf__navbar-item')
//         for(const collapsible of collapsibles)
//         {
//             collapsible.onclick = e => {
//                 collapsible.parentNode.classList.remove('static');
//                 collapsible.parentNode.classList.toggle('active');
//             }
//         }
//     }
// }

// window.addEventListener('load', e => PfNavbarAdapter.setup());