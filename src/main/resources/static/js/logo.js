function logo(){
    $("#div1").fadeIn(4000);
    $("#div2").fadeIn(8000);
    $("#div3").fadeIn(12000);
    document.getElementById("div1").innerHTML = "CLS";


    if(window.location.pathname != "/index" && window.location.pathname != "/"){
        const hamburger = document.querySelector('.hamburger');
        const nav = document.querySelector('.navigation');

        const handleClick = () => {
            hamburger.classList.toggle('hamburger--active');
            nav.classList.toggle('navigation--active');
        }
        hamburger.addEventListener('click', handleClick);
    }

}

