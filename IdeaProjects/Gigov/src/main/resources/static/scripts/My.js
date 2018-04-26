$(document).ready(function () {

    htmlbodyHeightUpdate()
    $( window ).resize(function() {
        htmlbodyHeightUpdate()
    });
    $( window ).scroll(function() {
        height2 = $('.main').height()
        htmlbodyHeightUpdate()
    });

    function htmlbodyHeightUpdate(){
        var height3 = $( window ).height()
        var height1 = $('.nav').height()+50
        height2 = $('.main').height()
        if(height2 > height3){
            $('html').height(Math.max(height1,height3,height2)+10);
            $('body').height(Math.max(height1,height3,height2)+10);
        }
        else
        {
            $('html').height(Math.max(height1,height3,height2));
            $('body').height(Math.max(height1,height3,height2));
        }

    }

    var searchResult;

    $(window).load(function () {
        $('#carousel').flexslider({
            animation: "slide",
            controlNav: false,
            animationLoop: false,
            slideshow: false,
            itemWidth: 210,
            itemMargin: 5,
            asNavFor: '#slider'
        });

        $('#slider').flexslider({
            animation: "slide",
            controlNav: false,
            animationLoop: false,
            slideshow: false,
            sync: "#carousel"
        });
    });

    function loadArticles(searchResult) {
        $('.p-4').empty();
        $('#title').text("Search result:");
        for (var i = 0; i < searchResult.length; i++) {
            var article = searchResult[i];
            var $pesho = $('search[id^="search"]:last');
            var num = parseInt($pesho.prop("id").match(/\d+/g), 10) + 1;
            var $klon = $pesho.clone().prop('id', 'search' + num);
            $klon.prop('style', 'display: block');
            $klon.find('#link').text(article.title).prop('href', '/article/' + article.id);
            $('#searchArticles').append($klon);
        }
    }

    /*search engine*/
    $("button").on("click", function () {
        var searchText = $("#search").val();
        console.log('searchText = ' + searchText);
        $.ajax({
            url: '/getAllArticles',
            type: "GET",
            success: function (articles) {
                var options = {
                    keys: ['title']
                };
                var fuse = new Fuse(articles, options);
                searchResult = fuse.search(searchText);
                console.log(searchResult[0].title);
                loadArticles(searchResult);
            },
            error: function (error) {
                console.log('pesho')
                console.log(error);
            }
        });
    });
});