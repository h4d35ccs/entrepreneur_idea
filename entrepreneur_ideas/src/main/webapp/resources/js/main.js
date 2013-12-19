// General Namespace
var EnterApp = {};

// Namespace for sync with backend
EnterApp.Sync = {};
EnterApp.Sync = function(){
	return {
		loadIdeas: function(callbacks){
			
			var ideasCollection = new IdeasCollection();
			
			ideasCollection.fetch({
				success: function(model, response, options){
					if(response.status=="ERROR"){
						callbacks.error(response);
					}else if(response.status=="SUCCESS"){
						callbacks.success(response);
					}
				},
				error: function(model, response, options){
					alert("The request to the server failed");
				}
			});
			
		},
		loadUsers: function(callbacks){
			
			var usersCollection = new UsersCollection();
			
			usersCollection.fetch({
				success: function(model, response, options){
					if(response.status=="ERROR"){
						callbacks.error(response);
					}else if(response.status=="SUCCESS"){
						callbacks.success(response);
					}
				},
				error: function(model, response, options){
					alert("The request to the server failed");
				}
			});
			
		},
		saveIdea: function(ideasAttributes, callbacks){
			ideaModel = new Idea();
			ideaModel.set("title", ideasAttributes.title);
			ideaModel.set("description", ideasAttributes.description);
			ideaModel.set("topic", ideasAttributes.topic);
			ideaModel.set("owner", ideasAttributes.userId);
			
			ideaModel.save(ideaModel.attributes, {
				success: function(model, response, options){
					if(response.status=="ERROR"){
						if(callbacks.error){
							callbacks.error(response);
						}
					}else if(response.status=="SUCCESS"){
						if(callbacks.success){
							callbacks.success(response);
						}
					}
				},
				error: function(model, response, options){
					alert("The request to the server failed");
				}
			});
		},
		editIdea: function(ideasAttributes, callbacks){
			ideaModel = new Idea();
			ideaModel.set("idIdea", ideasAttributes.idIdea);
			ideaModel.set("title", ideasAttributes.title);
			ideaModel.set("description", ideasAttributes.description);
			
			ideaModel.save(ideaModel.attributes, {
				success: function(model, response, options){
					if(response.status=="ERROR"){
						if(callbacks.error){
							callbacks.error(response);
						}
					}else if(response.status=="SUCCESS"){
						if(callbacks.success){
							callbacks.success(response);
						}
					}
				},
				error: function(model, response, options){
					alert("The request to the server failed");
				}
			});
		},
		getIdea: function(id, callbacks){
			if(id){
				ideaModel = new Idea();
				ideaModel.idIdea = id;
				ideaModel.fetch({
					clearData: true,
					success: function(model, response, options){
						if(response.status=="ERROR"){
							if(callbacks.error){
								callbacks.error(response);
							}
						}else if(response.status=="SUCCESS"){
							if(callbacks.success){
								callbacks.success(response);
							}
						}
					},
					error: function(model, response, options){
						alert("The request to the server failed");
					}
				});
			}
			else{
				if(callbacks.error){
					callbacks.error();
				}
				else{
					alert("the idea could not be obtained");
				}
			}
		},
		saveComment: function(commentAttributes, callbacks){
			commentModel = new Comment();
			commentModel.set("text", commentAttributes.text);
			// two assignments to match model in view and in adapter
			commentModel.set("idUser", commentAttributes.owner+"");
			commentModel.set("owner", commentAttributes.owner);
			commentModel.set("idea", commentAttributes.idIdea);
			commentModel.set("idIdea", commentAttributes.idIdea+"");
						
			commentModel.save(commentAttributes.attributes, {
				success: function(model, response, options){
					if(response.status=="ERROR"){
						if(callbacks.error){
							callbacks.error(response);
						}
					}else if(response.status=="SUCCESS"){
						if(callbacks.success){
							callbacks.success(response);
						}
					}
				},
				error: function(model, response, options){
					alert("The request to the server failed");
				}
			});
		},
		loadComments: function(idIdea, callbacks){
			var commentsCollection = new CommentsCollection([], {idIdea: idIdea});
			
			commentsCollection.fetch({
				success: function(model, response, options){
					if(response.status=="ERROR"){
						callbacks.error(response);
					}else if(response.status=="SUCCESS"){
						callbacks.success(response);
					}
				},
				error: function(model, response, options){
					alert("The request to the server failed");
				}
			});
			
		},
		doLogin: function(inputData, callbacks){
			loginUser = new LoginUser();
			loginUser.set("username",inputData.username);
			loginUser.set("password",inputData.password);
			loginUser.save(loginUser.attributes,{
				success: function(model, response, options){
					if(response.status=="ERROR"){
						if(callbacks.error){
							callbacks.error(response);	
						}
					}else if(response.status=="SUCCESS"){
						if(callbacks.success){
							callbacks.success(response);
						}
					}
				},
				error: function(model, response, options){
					alert("The request to the server failed");
					
				}
			});
		},
		doLogout: function(callbacks){
			$.ajax({
		   		type: "GET",
		   		url: "rest/loginLogout/logout",
		   		data: '',
		   		dataType: "json",
				success: function(response){
					if(response.status=="ERROR"){
						if(callbacks.error){
							callbacks.error(response);	
						}
					}else if(response.status=="SUCCESS"){
						if(callbacks.success){
							callbacks.success(response);
						}
					}
				},
				error: function(response){
					alert("The request to the server failed");
				}
		   	});
		},
		getUserProfile: function(callbacks){
			$.ajax({
		   		type: "GET",
		   		url: "rest/user/loggedUser",
		   		data: '',
		   		dataType: "json",
				success: function(response){
					if(response.status=="ERROR"){
						if(callbacks.error){
							callbacks.error(response);	
						}
					}else if(response.status=="SUCCESS"){
						if(callbacks.success){
							callbacks.success(response);
						}
					}
				},
				error: function(response){
					alert("The request to the server failed");
				}
		   	});
		},
		saveNewUser: function(userAttributes, callbacks){
			userModel = new User();
			userModel.set("email", userAttributes.email);
			userModel.set("name", userAttributes.name);
			userModel.set("lastname", userAttributes.lastname);
			userModel.set("nickname", userAttributes.nickname);
			userModel.set("password", userAttributes.password);
			
			userModel.save(userModel.attributes, {
				success: function(model, response, options){
					if(response.status=="ERROR"){
						if(callbacks.error){
							callbacks.error(response);
						}
					}else if(response.status=="SUCCESS"){
						if(callbacks.success){
							callbacks.success(response);
						}
					}
				},
				error: function(model, response, options){
					alert("The request to the server failed");
				}
			});
		},
		removeIdea: function(idIdea, callbacks){
			var idea = new Idea();
    		idea.set("idIdea", idIdea);
    		idea.destroy({
    			success:function(model, response, options){
    				if(response.status=="ERROR"){
						if(callbacks.error){
							callbacks.error(response);
						}
					}else if(response.status=="SUCCESS"){
						if(callbacks.success){
							callbacks.success(response);
						}
					}
    			}, 
    			error:function(model, response, options){
    				alert("The request to the server failed");
    			}
    		});
		},
		removeComment: function(idComment, callbacks){
			var comment = new Comment();
			comment.set("idComment", idComment);
			comment.destroy({
    			success:function(model, response, options){
    				if(response.status=="ERROR"){
						if(callbacks.error){
							callbacks.error(response);
						}
					}else if(response.status=="SUCCESS"){
						if(callbacks.success){
							callbacks.success(response);
						}
					}
    			}, 
    			error:function(model, response, options){
    				alert("The request to the server failed");
    			}
    		});
		}
	};
}();

// Example of use
// EnterApp.Sync.saveIdea({
//	title: "ey este titulo", 
//	description: "tremenda ideota", 
//	topic: 1},
//	function(response){
//		// here is for success
//	},
//	function(response){
//		// here is for error
//	}
//);

//Namespace for UI
EnterApp.UI = {};
EnterApp.UI = function(){
	return {
	};
}();

EnterApp.Utils = {};
EnterApp.Utils = function(){
	var QueryString = function () {
	  var query_string = {};
	  var query = window.location.search.substring(1);
	  var vars = query.split("&");
	  for (var i=0;i<vars.length;i++) {
	    var pair = vars[i].split("=");
	    	// If first entry with this name
	    if (typeof query_string[pair[0]] === "undefined") {
	      query_string[pair[0]] = pair[1];
	    	// If second entry with this name
	    } else if (typeof query_string[pair[0]] === "string") {
	      var arr = [ query_string[pair[0]], pair[1] ];
	      query_string[pair[0]] = arr;
	    	// If third or later entry with this name
	    } else {
	      query_string[pair[0]].push(pair[1]);
	    }
	  } 
	    return query_string;
	} ();
	
	return {
		queryStringParam: function(param){
			return QueryString[param];
		}
	};
}();

//End of Document

