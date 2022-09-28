

$(document).ready(function(){
 if(!(filename === null)){
  var element = document.querySelector(".submit_file-button");
  element.style["background"] = "#1bbc9b";
 }
});


$(function(){
  $('.input_file').on('change', function(){
  filename = this.value.replace(/.*[\/\\]/, '');
  var element = document.querySelector(".submit_file-button");
  element.style["background"] = "#bcac1b";
  console.log('file name changed!!');
  });
})

$(function(){

   $('.doc_proceed_btn').on('click',function()
   {
     console.log(filename);
   });

})