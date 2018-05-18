package edu.training

import grails.artefact.TagLibrary
import grails.gsp.TagLib

@TagLib
class LayoutTagLib implements TagLibrary{

    static namespace = "layout"
    static allowedColors = ['default','info','danger','primary','success','warning','yellow','green','red']


    /**
     * button element.
     *
     * @attr title
     * @attr id REQUIRED
     * @attr opened
     * @attr color
     * @attr buttons
     */
    Closure collapsePanel = { attrs, body  ->

        String title = attrs.remove("title")
        String id = attrs.remove("id")
        String color = attrs.remove("color")?:"info"
        List buttons = attrs.remove("buttons")?.asType(List)

        if(!(color in allowedColors)){
            throw new Exception("color must be in ${allowedColors}")
        }

        booleanToAttribute(attrs,"opened")

        out << """  

<div class="row ${attrs.class}">
    <div class="col-lg-12">
        <div class="panel panel-${color}">
            <div class="panel-heading">
                <a data-toggle="collapse" data-parent="#accordion" href="#${id}">
                    <h4 class="panel-title"> 
                        <i class="fa fa-search" ></i>
                        ${title}
                    </h4>
                </a>
            </div>
            <div id="${id}" class="panel-collapse collapse ${attrs.opened?"in":""}">
                <div class="panel-body">

                    <div class="col-lg-12">

                      """

        out << body()
                    out << """  
                      
                    </div>

                </div>

                <div class="panel-footer text-center">
                """


                buttons.each {
                    out << it
                }



        out << """
                </div>

            </div>
        </div>
    </div>
</div>

  """

    }

    /**
     * button element.
     *
     * @attr title
     * @attr color
     * @attr buttons
     */
    Closure showPanel = { attrs, body  ->

        String title = attrs.remove("title")
        String color = attrs.remove("color")?:"info"
        List buttons = attrs.remove("buttons")?.asType(List)

        if(!(color in allowedColors)){
            throw new Exception("color must be in ${allowedColors}")
        }


        out << """  

<div class="row">
    <div class="col-lg-12">
        <div class="panel panel-${color}">
            <div class="panel-heading">
                    <h4 class="panel-title"> 
                        <i class="fa fa-eye" ></i>
                        ${title}
                    </h4>
                </a>
            </div>
                <div class="panel-body">

                    <div class="col-lg-12">

                      """

        out << body()
                    out << """  
                      
                    </div>

                </div>

                <div class="panel-footer text-center">
                """


                buttons.each {
                    out << it
                }



        out << """
                </div>

        </div>
    </div>
</div>

  """

    }



    /**
     * button element.
     *
     * @attr title
     * @attr color
     * @attr buttons
     * @attr action the name of the action to use in the link, if not specified the default action will be linked
     * @attr controller the name of the controller to use in the link, if not specified the current controller will be linked
     * @attr id The id to use in the link
     * @attr url A map containing the action,controller,id etc.
     * @attr name A value to use for both the name and id attribute of the form tag
     * @attr useToken Set whether to send a token in the request to handle duplicate form submissions. See Handling Duplicate Form Submissions
     * @attr method the form method to use, either 'POST' or 'GET'; defaults to 'POST'
     * @attr withAttachment
     */
    Closure formPanel = { attrs, body  ->

        String title = attrs.remove("title")?:""
        String color = attrs.remove("color")
        List buttons = attrs.remove("buttons")?.asType(List)

        if(color && !(color in allowedColors)){
            throw new Exception("color must be in ${allowedColors}")
        }

        booleanToAttribute(attrs,"withAttachment")

        out << """ <div class="col-lg-12">
                     <div class="panel panel-default ${color?"panel-${color}":""}" >
                     <div class="panel-heading">
                        ${title}
                     </div>
               """

        if(attrs.withAttachment){
            attrs.enctype = "multipart/form-data"
        }

        out << g.form(attrs) {

        out << """ <div class="panel-body">
               """

            out << body()

            out << """  </div>
                      <div class="panel-footer text-center">
                  """

            buttons.each {
                out << it
            }

            out << """ </div>"""

        }

        out <<"""   </div>
                </div>   """
    }

    private void booleanToAttribute(Map attrs, String attrName) {
        def attrValue = attrs.remove(attrName)
        if (attrValue instanceof CharSequence) {
            attrValue = attrValue.toString().trim()
        }
        // If the value is the same as the name or if it is a boolean value,
        // reintroduce the attribute to the map according to the w3c rules, so it is output later
        if ((attrValue instanceof Boolean && attrValue) ||
                (attrValue instanceof String && (((String)attrValue).equalsIgnoreCase("true") || ((String)attrValue).equalsIgnoreCase(attrName)))) {
            attrs.put(attrName, attrName)
        } else if (attrValue instanceof String && !((String)attrValue).equalsIgnoreCase("false")) {
            // If the value is not the string 'false', then we should just pass it on to
            // keep compatibility with existing code
            attrs.put(attrName, attrValue)
        }
    }

}

