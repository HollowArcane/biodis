class Page
{
    constructor(btnPdf, balanceSection, chartSection)
    {
        this.btnPdf = btnPdf;
        this.balanceSection = balanceSection;
        this.chartSection = chartSection;
    }

    init()
    {
        this.btnPdf.onclick = () =>
            this.chartSection.exportPDF(file => {
                openTab(`/stock/mvt-product-stock/pdf?chartImage=${file}&chartTitle=${this.chartSection.generateTitle()}&${this.balanceSection.form.queryParams([
                    'ndays', 'date', 'idCategory', 'idSubcategory'
                ])}`);
            });

        this.balanceSection.init();
        this.chartSection.init();
    }
}

window.addEventListener('load', e => {
    new Page(
        document.getElementById('btn-pdf'),
        new BalanceSection(
            document.getElementById('form'),
            document.getElementById('table'),
            document.getElementById('idCategory'),
            document.getElementById('idSubcategory'),
        ),
        new ChartSection(
            document.getElementById('formFilter'),
            document.getElementById('formConfig'),
            document.getElementById('chart'),
            document.getElementById('chartIdCategory'),
            document.getElementById('chartIdSubcategory'),
            document.getElementById('message'),
        )
    )
    .init()
})