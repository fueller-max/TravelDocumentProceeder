//Proceed each page loading...
$(document).ready(function(){
 if(!(filename === null)){
  var element = document.querySelector(".submit_file-button");
  element.style["background"] = "#1bbc9b";
 }

});

//Load button handler
//Change the background colour once the file has been chosen
$(function(){
  $('.input_file').on('change', function(){
  filename = this.value.replace(/.*[\/\\]/, '');
  var element = document.querySelector(".submit_file-button");
  element.style["background"] = "#bcac1b";
  console.log('file name changed!!');
  });
})



//Proceed the file. When pressed send the request to the server to proceed the file
//and after the file automatically downloaded
$(function(){

   $('.proceed_button_wrapper').on('click',function()
   {
    console.log(filename);
    var fileNameRequest = "/files/" + filename;
    $.ajax({
         url : fileNameRequest,
         type : 'GET',
         xhrFields: {
                     responseType: 'blob'
                 },
         success: function (data, status, xhr)
                  {
          const disposition = xhr.getResponseHeader('Content-Disposition');

            //Extract the file name from the response and clean it up
                 fileName = disposition.split(/;(.+)/)[1].split(/=(.+)/)[1]
                 if (fileName.toLowerCase().startsWith("utf-8''"))
                     fileName = decodeURIComponent(filename.replace("utf-8''", ''))
                 else
                     fileName = fileName.replace(/['"]/g, '')

                 var blob = new Blob([data], { type: "application/octetstream" });

                 var url = window.URL || window.webkitURL;
                 var link = url.createObjectURL(blob);

                 var a = $("<a />");
                 a.attr("download", fileName);
                 a.attr("href", link);
                 $("body").append(a);
                 a[0].click();
                 $("body").remove(a);
                  },
          error : function(data, status, xhr)
                 {
                 alert(status);
                 console.log('Error has occurred!')
                 }
          });
    });

});












