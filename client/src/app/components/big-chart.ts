// eslint-disable-next-line @typescript-eslint/no-unused-vars
import Chart, {ChartData, ChartOptions, ChartTooltipItem} from 'chart.js'


// Set new default font family and font color to mimic Bootstrap's default styling


const global = Chart.defaults.global;
global.defaultFontFamily = 'Open Sans';
global.defaultFontColor = '#858796';
global.defaultFontSize = 13;
global.responsive = true;
global.layout = {padding: 0}
global.legend = {
    display: false,
    position: 'bottom',
    labels: {
        usePointStyle: true,
        padding: 16
    }
}


export function number_format(number: number | string, decimals: number, dec_point: undefined, thousands_sep: string) {
    // *     example: number_format(1234.56, 2, ',', ' ');
    // *     return: '1 234,56'
    number = (number + '').replace(',', '').replace(' ', '');
    let n = !isFinite(+number) ? 0 : +number,
        prec: number = !isFinite(+decimals) ? 0 : Math.abs(decimals),
        sep = (typeof thousands_sep === 'undefined') ? ',' : thousands_sep,
        dec = (typeof dec_point === 'undefined') ? '.' : dec_point,
        s: string[],
        toFixedFix = function (n: number, prec: number) {
            const k = Math.pow(10, prec);
            return '' + Math.round(n * k) / k;
        };
    // Fix for IE parseFloat(0.55).toFixed(0) = 0;
    s = (prec ? toFixedFix(n, prec) : '' + Math.round(n)).split('.');
    if (s[0].length > 3) {
        s[0] = s[0].replace(/\B(?=(?:\d{3})+(?!\d))/g, sep);
    }
    if ((s[1] || '').length < prec) {
        s[1] = s[1] || '';
        s[1] += new Array(prec - s[1].length + 1).join('0');
    }
    return s.join(dec);
}


export function drawChart(chartData:ChartData,options:ChartOptions): Chart {
    const ctx = document.getElementById("myBigChart") as HTMLCanvasElement;
    return new Chart(ctx, {
        type: 'line',
        data: chartData,
        options:options

    })
}

// Area Chart Example

