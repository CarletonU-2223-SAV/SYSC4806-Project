document.addEventListener('DOMContentLoaded' , function() {

    const myNumberInput = document.getElementById('orderAmount');

    myNumberInput.addEventListener('change', function() {

        document.getElementById('changeOrderForm').submit();
    });
});
