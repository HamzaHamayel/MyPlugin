package edu.training

import grails.artefact.TagLibrary
import grails.gsp.TagLib


@TagLib
class NavBarTagLib implements TagLibrary{

    static namespace = 'nav'

    static systemLanguages = ["ar","en"]

    def languages = { args ->
        params.remove("lang")
        out << """
            <li class="dropdown">
                <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                    <i class="fa fa-language"></i> <i class="fa fa-caret-down"></i>
                </a>
                <ul class="dropdown-menu dropdown-user">"""

        systemLanguages.each {lang->
            out << "<li><a href='${createLink(controller: controllerName,action: actionName,params: params)}?lang=${lang}'>${message(code: 'language.'+lang)}</a></li>"
        }
        out << """  </ul>
            </li>"""
    }

}