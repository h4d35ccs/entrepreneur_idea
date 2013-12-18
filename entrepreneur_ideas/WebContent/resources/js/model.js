// Model ----------------------------->

var CustomBaseModel = Backbone.Model.extend({
	actionUrls:function(){},
	sync:function(method, model, options){
        
		options = _(options).clone();
		if(!options.data && !options.clearData){
			options.data = JSON.stringify(model);
		}
		options.contentType = "application/json";
		options.dataType = "json";
		
		switch(method){
			case "create":
				options.type = "POST";
				options.url = model.actionUrls().createUrl;
			break;
			case "update":
				options.type = "POST";
				options.url = model.actionUrls().updateUrl;
			break;
			case "read":
				options.contentType = "";
				options.type = "GET";
				options.url = model.actionUrls().readUrl;
			break;
			case "delete":
				options.type = "POST";
				options.url = model.actionUrls().deleteUrl;
			break;
		}
		
		$.ajax(options);
    }
});

var Idea = CustomBaseModel.extend({
	idAttribute: "idIdea",
	defaults: {
	    "title":     "",
	    "description":    "",
	    "topic" : "",
	    "creationDate" : "",
	    "owner" : 0
	},
	actionUrls:function(){
		return {
			createUrl: "rest/idea/create",
			updateUrl: "rest/idea/edit",
			readUrl: "rest/idea/get/"+this.idIdea,
			deleteUrl: "rest/idea/delete/"
		};
	}
});

var Comment = CustomBaseModel.extend({
	idAttribute: "idComment",
	defaults: {
	    "text":     "",
	    "idea":     0,
	    "owner" : 0
	},
	actionUrls:function(){
		return {
			createUrl: "rest/idea/comment/create",
			updateUrl: "rest/idea/comment/edit",
			readUrl: "rest/idea/comment/get/"+this.idComment,
			deleteUrl: "rest/idea/comment/delete"
		};
	}
});

var User = CustomBaseModel.extend({
	idAttribute: "idUser",
	defaults: {
	    "email":     "",
	    "name":     "",
	    "lastname" : "",
	    "nickname" : "",
	    "password" : ""
	},
	actionUrls:function(){
		return {
			createUrl: "rest/user/signup",
		};
	}
});

var LoginUser = CustomBaseModel.extend({
	defaults: {
	    "username" : "",
	    "password" : ""
	},
	actionUrls:function(){
		return {
			createUrl: "rest/loginLogout/login",
		};
	}
});


// Collections ------------------------------------->

var IdeasCollection = Backbone.Collection.extend({
    url: "rest/idea/list"
});

var UsersCollection = Backbone.Collection.extend({
    url: "rest/user/list"
});

var CommentsCollection = Backbone.Collection.extend({
	initialize: function(models, options){
		this.url = "rest/idea/comment/list/"+options.idIdea;
	}
});


