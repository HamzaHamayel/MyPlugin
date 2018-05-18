package edu.training

import grails.artefact.TagLibrary
import grails.gsp.TagLib
import org.grails.buffer.GrailsPrintWriter
import org.grails.core.artefact.DomainClassArtefactHandler
import org.grails.encoder.CodecLookup
import org.grails.encoder.Encoder
import org.springframework.context.MessageSourceResolvable
import org.springframework.core.convert.ConversionService
import org.springframework.web.servlet.support.RequestContextUtils
import org.springframework.web.servlet.support.RequestDataValueProcessor

@TagLib
class InputTagLib implements TagLibrary{

    static namespace = "field"

    CodecLookup codecLookup
    RequestDataValueProcessor requestDataValueProcessor
    ConversionService conversionService
    private List<String> booleanAttributes = ['disabled', 'checked', 'readonly','required']



    /**
     * Creates a new  static text.
     *
     * @attr label REQUIRED the field label
     * @attr value the field value
     * @attr size the field size
     */
    Closure staticText = { attrs ->

        String label = attrs.remove("label")
        String value = attrs.remove("value")
        String size = attrs.remove("size")?:"4"

        out << """<div class="col-lg-${size} form-group">
                                            <label>${label}</label>
                                            <p class="form-control-static">${value?:""}</p>
                                        </div>  """
    }


    /**
     * Creates a new text field.
     *
     * @attr name REQUIRED the field name
     * @attr label REQUIRED the field label
     * @attr value the field value
     * @attr required
     * @attr icon
     * @attr withIcon
     */
    Closure text = { attrs ->
        attrs.type = "text"
        attrs.tagName = "text"
        attrs.icon = attrs.remove("icon")?:"fa fa-pencil "
        attrs.withIcon = attrs.remove("withIcon")?:"true"
        fieldImpl(out, attrs)
    }

    /**
     * Creates a new mail field.
     *
     * @attr name REQUIRED the field name
     * @attr label REQUIRED the field label
     * @attr value the field value
     * @attr required
     * @attr withIcon
     */
    Closure mail = { attrs ->
        attrs.type = "text"
        attrs.class = " mailInput "+ attrs.remove("class")
        attrs.tagName = "text"
        attrs.icon = "fa fa-envelope"
        attrs.withIcon = attrs.remove("withIcon")?:"true"
        fieldImpl(out, attrs)
    }

    /**
     * Creates a new integer field.
     *
     * @attr name REQUIRED the field name
     * @attr label REQUIRED the field label
     * @attr value the field value
     * @attr required
     * @attr withIcon
     */
    Closure integer = { attrs ->
        attrs.type = "text"
        attrs.class = " integerInput "+ attrs.remove("class")
        attrs.tagName = "text"
        attrs.iconText = "123"
        attrs.withIcon = attrs.remove("withIcon")?:"true"
        fieldImpl(out, attrs)
    }

    /**
     * Creates a new integer field.
     *
     * @attr name REQUIRED the field name
     * @attr label REQUIRED the field label
     * @attr value the field value
     * @attr required
     * @attr withIcon
     */
    Closure currency = { attrs ->
        attrs.type = "text"
        attrs.class = " currencyInput "+ attrs.remove("class")
        attrs.tagName = "text"
        attrs.icon = "fa fa-dollar"
        attrs.withIcon = attrs.remove("withIcon")?:"true"
        fieldImpl(out, attrs)
    }
    /**
     * Creates a new date field.
     *
     * @attr name REQUIRED the field name
     * @attr label REQUIRED the field label
     * @attr value the field value
     * @attr required
     * @attr withIcon
     */
    Closure date = { attrs ->
        attrs.type = "text"
        attrs.class = " dateInput "+ attrs.remove("class")
        attrs.tagName = "text"
        attrs.icon = "fa fa-calendar"
        attrs.readonly = "true"
        attrs.withIcon = attrs.remove("withIcon")?:"true"
        fieldImpl(out, attrs)
    }

    /**
     * Creates a new password field.
     *
     * @attr name REQUIRED the field name
     * @attr label REQUIRED the field label
     * @attr value the field value
     * @attr required
     * @attr icon
     * @attr withIcon
     */
    Closure password = { attrs ->
        attrs.type = "password"
        attrs.tagName = "password"
        attrs.icon = ""
        attrs.withIcon = attrs.remove("withIcon")?:"true"
        fieldImpl(out, attrs)
    }

    /**
     * Creates a new file field.
     *
     * @attr name REQUIRED the field name
     * @attr label REQUIRED the field label
     * @attr value the field value
     * @attr required
     * @attr icon
     * @attr withIcon
     */
    Closure file = { attrs ->
        attrs.type = "file"
        attrs.tagName = "file"
        attrs.icon = "fa fa-upload"
        attrs.withIcon = attrs.remove("withIcon")?:"true"

        Object value = attrs.remove("value")
        if(value){
            attrs.customImage = """<img src="data:image/png;base64,${value?.encodeBase64()}" />"""
        }

        fieldImpl(out, attrs)
    }

    /*
    * @attr name REQUIRED the select name
    * @attr id the DOM element id - uses the name attribute if not specified
    * @attr from REQUIRED The list or range to select from
    * @attr keys A list of values to be used for the value attribute of each "option" element.
    * @attr optionKey By default value attribute of each &lt;option&gt; element will be the result of a "toString()" call on each element. Setting this allows the value to be a bean property of each element in the list.
    * @attr optionValue By default the body of each &lt;option&gt; element will be the result of a "toString()" call on each element in the "from" attribute list. Setting this allows the value to be a bean property of each element in the list.
    * @attr value The current selected value that evaluates equals() to true for one of the elements in the from list.
    * @attr multiple boolean value indicating whether the select a multi-select (automatically true if the value is a collection, sets to single-select either if multiple is missing or it is explicitly set to false)
    * @attr valueMessagePrefix By default the value "option" element will be the result of a "toString()" call on each element in the "from" attribute list. Setting this allows the value to be resolved from the I18n messages. The valueMessagePrefix will be suffixed with a dot ('.') and then the value attribute of the option to resolve the message. If the message could not be resolved, the value is presented.
    * @attr noSelection A single-entry map detailing the key and value to use for the "no selection made" choice in the select box. If there is no current selection this will be shown as it is first in the list, and if submitted with this selected, the key that you provide will be submitted. Typically this will be blank - but you can also use 'null' in the case that you're passing the ID of an object
    * @attr disabled boolean value indicating whether the select is disabled or enabled (defaults to false - enabled)
    * @attr readOnly boolean value indicating whether the select is read only or editable (defaults to false - editable)
    * @attr required boolean value indicating whether the select is required
    * @attr dataAttrs a Map that adds data-* attributes to the &lt;option&gt; elements. Map's keys will be used as names of the data-* attributes like so: data-${key} (i.e. with a "data-" prefix). The object belonging to a Map's key determines the value of the data-* attribute. It can be a string referring to a property of beans in {@code from}, a Closure that accepts an item from {@code from} and returns the value or a List that contains a value for each of the &lt;option&gt;s.
    * @attr label
    */
    Closure select = { attrs ->
        if (!attrs.name) {
            throwTagError("Tag [select] is missing required attribute [name]")
        }
        if (!attrs.containsKey('from')) {
            throwTagError("Tag [select] is missing required attribute [from]")
        }
        def messageSource = grailsAttributes.getApplicationContext().getBean("messageSource")
        def locale = RequestContextUtils.getLocale(request)
        def writer = out
        def from = attrs.remove('from')
        def keys = attrs.remove('keys')
        def optionKey = attrs.remove('optionKey')
        def optionDisabled = attrs.remove('optionDisabled')
        def optionValue = attrs.remove('optionValue')
        def value = attrs.remove('value')
        def dataAttrs = attrs.remove('dataAttrs')
        def label = attrs.remove('label')
        if (value instanceof Collection && attrs.multiple == null) {
            attrs.multiple = 'multiple'
        }
        if(attrs.multiple == false){
            attrs.remove('multiple')
        }
        if (value instanceof CharSequence) {
            value = value.toString()
        }
        def valueMessagePrefix = attrs.remove('valueMessagePrefix')

        if(attrs["noSelection"] == null){
            attrs.noSelection = ['':message(code:'select.label',default: 'select')]
        }

        def noSelection = attrs.remove('noSelection')
        if (noSelection != null) {
            noSelection = noSelection.entrySet().iterator().next()
        }
        booleanToAttribute(attrs, 'disabled')
        booleanToAttribute(attrs, 'readonly')
        booleanToAttribute(attrs, 'required')

        writer << """ <div class="form-group">"""
        writer << """ <label>${label}</label> """
        if(attrs.required){
            writer << """<span style='color:red'>*<span>"""
        }
        writer << """<select class="form-control" """
        // process remaining attributes
        outputAttributes(attrs, writer, true)

        writer << '>'
        writer.println()

        if (noSelection) {
            renderNoSelectionOptionImpl(writer, noSelection.key, noSelection.value, value)
            writer.println()
        }

        // create options from list
        from.eachWithIndex {el, i ->
            def keyDisabled
            def keyValue
            def dataAttrsMap = getDataAttr(el, dataAttrs, i)
            writer << '<option '
            if (keys) {
                keyValue = keys[i]
                writeValueAndCheckIfSelected(attrs.name, keyValue, value, writer, dataAttrsMap)
            }
            else if (optionKey) {
                def keyValueObject
                if (optionKey instanceof Closure) {
                    keyValue = optionKey(el)
                }
                else if (el != null && optionKey == 'id' && grailsApplication.getArtefact(DomainClassArtefactHandler.TYPE, el.getClass().name)) {
                    keyValue = el.ident()
                    keyValueObject = el
                }
                else {
                    keyValue = el[optionKey]
                    keyValueObject = el
                }
                if(optionDisabled) {
                    if (optionDisabled instanceof Closure) {
                        keyDisabled = optionDisabled(el)
                    }
                    else {
                        keyDisabled = el[optionDisabled]
                    }
                }
                writeValueAndCheckIfSelected(attrs.name, keyValue, value, writer, dataAttrsMap, keyValueObject, keyDisabled)
            }
            else {
                keyValue = el
                writeValueAndCheckIfSelected(attrs.name, keyValue, value, writer, dataAttrsMap)
            }
            writer << '>'
            if (optionValue) {
                if (optionValue instanceof Closure) {
                    writer << optionValue(el).toString().encodeAsHTML()
                }
                else {
                    writer << el[optionValue].toString().encodeAsHTML()
                }
            }
            else if (el instanceof MessageSourceResolvable) {
                writer << messageSource.getMessage(el, locale)
            }
            else if (valueMessagePrefix) {
                def message = messageSource.getMessage("${valueMessagePrefix}.${keyValue}", null, null, locale)
                if (message != null) {
                    writer << message.encodeAsHTML()
                }
                else if (keyValue && keys) {
                    def s = el.toString()
                    if (s) writer << s.encodeAsHTML()
                }
                else if (keyValue) {
                    writer << keyValue.encodeAsHTML()
                }
                else {
                    def s = el.toString()
                    if (s) writer << s.encodeAsHTML()
                }
            }
            else {
                def s = el.toString()
                if (s) writer << s.encodeAsHTML()
            }
            writer << '</option>'
            writer.println()
        }
        // close tag
        writer << '</select>'
        writer << """ </div>"""
    }


    def fieldImpl(GrailsPrintWriter out, Map attrs) {
        resolveAttributes(attrs)
        attrs.value = processFormFieldValueIfNecessary(attrs.name, attrs.value, attrs.type)
        String clazz = attrs.remove("class")
        String icon = attrs.remove("icon")?:""
        String iconText = attrs.remove("iconText")?:""
        Object customImage = attrs.remove("customImage")
        attrs.class = " form-control " + clazz
        Object object = attrs.remove("object")

        Boolean hasError = false
        if(object) {
            hasError = object?.errors?.allErrors?.findAll { it.field == attrs["name"] }?.size() > 0
        }

        booleanToAttribute(attrs,"required")
        booleanToAttribute(attrs,"withIcon")

        if(customImage){
            out << """<div class="col-lg-12">"""
            out << """<div class="col-lg-6">"""
        }

        out << """<div id="formGroup_${attrs["name"]}" class="form-group ${hasError?" has-error ":""}">"""
        out << """<label>${attrs.remove('label')}"""
        if(attrs.required){
            out << """<span style='color:red'>*<span>"""
        }
        out << """</label>"""
        if(attrs.withIcon) {
            out << """<div class="input-group">"""
        }
        out << "<input type=\"${attrs.remove('type')}\" "


        outputAttributes(attrs, out, true)

        out << "/>"
        if(attrs.withIcon) {
            out << "<span class=\"input-group-addon\"> ${iconText?"""  <i>${iconText}</i> """:""} ${icon?""" <i class='${icon}' ></i> """:""} </span>"
            out << "</div>"
        }

        out << "</div>"

        if(customImage) {
            out << "</div>"
            out << """<div class="col-lg-6">"""
            out << customImage
            out << "</div>"
            out << "</div>"
        }


    }


    /**
     * Check required attributes, set the id to name if no id supplied, extract bean values etc.
     */
    void resolveAttributes(Map attrs) {
        if (!attrs.name && !attrs.field) {
            throwTagError("Tag [${attrs.tagName}] is missing required attribute [name] or [field]")
        }

        attrs.remove('tagName')

        def val = attrs.remove('bean')
        if (val) {
            if (attrs.name.indexOf('.')) {
                attrs.name.split('\\.').each {val = val?."$it"}
            }
            else {
                val = val[name]
            }
            attrs.value = val
        }
        attrs.value = attrs.value != null ? attrs.value : "" // can't use ?: since 0 is groovy false

        booleanAttributes.each {
            booleanToAttribute(attrs, it)
        }
    }



    /**
     * Dump out attributes in HTML compliant fashion.
     */
    void outputAttributes(Map attrs, GrailsPrintWriter writer, boolean useNameAsIdIfIdDoesNotExist = false) {
        attrs.remove('tagName') // Just in case one is left
        Encoder htmlEncoder = codecLookup?.lookupEncoder('HTML')
        attrs.each { k, v ->
            if (v != null) {
                writer << k
                writer << '="'
                writer << (htmlEncoder != null ? htmlEncoder.encode(v) : v)
                writer << '" '
            }
        }
        if (useNameAsIdIfIdDoesNotExist) {
            outputNameAsIdIfIdDoesNotExist(attrs, writer)
        }
    }


    private processFormFieldValueIfNecessary(name, value, type) {
        if (requestDataValueProcessor != null) {
            return requestDataValueProcessor.processFormFieldValue(request, name, "${value}", type)
        }
        return value
    }

    private void outputNameAsIdIfIdDoesNotExist(Map attrs, GrailsPrintWriter out) {
        if (!attrs.containsKey('id') && attrs.containsKey('name')) {
            Encoder htmlEncoder = codecLookup?.lookupEncoder('HTML')
            out << 'id="'
            out << (htmlEncoder != null ? htmlEncoder.encode(attrs.name) : attrs.name)
            out << '" '
        }
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


    private void writeValueAndCheckIfSelected(selectName, keyValue, value, writer, dataAttrsMap) {
        writeValueAndCheckIfSelected(selectName, keyValue, value, writer, dataAttrsMap, null)
    }
    private void writeValueAndCheckIfSelected(selectName, keyValue, value, writer, dataAttrsMap, el) {
        writeValueAndCheckIfSelected(selectName, keyValue, value, writer, dataAttrsMap, el, null)
    }

    private void writeValueAndCheckIfSelected(selectName, keyValue, value, writer, dataAttrsMap, el, keyDisabled) {

        boolean selected = false
        def keyClass = keyValue?.getClass()
        if (keyClass.isInstance(value)) {
            selected = (keyValue == value)
        }
        else if (value instanceof Collection) {
            // first try keyValue
            selected = value.contains(keyValue)
            if (!selected && el != null) {
                selected = value.contains(el)
            }
        }
        // GRAILS-3596: Make use of Groovy truth to handle GString <-> String
        // and other equivalent types (such as numbers, Integer <-> Long etc.).
        else if (keyValue == value) {
            selected = true
        }
        else if (keyClass && value != null) {
            try {
                value = conversionService.convert(value, keyClass)
                selected = keyValue == value
            }
            catch (e) {
                // ignore
            }
        }
        keyValue = processFormFieldValueIfNecessary(selectName, "${keyValue}","option")
        writer << "value=\"${keyValue.toString().encodeAsHTML()}\" "

        if(dataAttrsMap) {
            dataAttrsMap.each {key, val->
                writer << "data-${key.toString().encodeAsHTML()}=\"${val.toString().encodeAsHTML()}\""
            }
        }
        if (selected) {
            writer << 'selected="selected" '
        }
        if(keyDisabled && !selected) {
            writer << 'disabled="disabled" '
        }
    }

    Closure renderNoSelectionOption = { noSelectionKey, noSelectionValue, value ->
        renderNoSelectionOptionImpl(out, noSelectionKey, noSelectionValue, value)
    }

    def renderNoSelectionOptionImpl(out, noSelectionKey, noSelectionValue, value) {
        // If a label for the '--Please choose--' first item is supplied, write it out
        out << "<option value=\"${(noSelectionKey == null ? '' : noSelectionKey)}\"${noSelectionKey == value ? ' selected="selected"' : ''}>${noSelectionValue.encodeAsHTML()}</option>"
    }

    private static Map getDataAttr(el, dataAttrs, index) {
        Map ret = [:]
        if(dataAttrs) {
            dataAttrs.each { k, v ->
                if (v instanceof CharSequence) {
                    //in case of bean property
                    ret[k] = el[v]
                } else if(v instanceof Closure) {
                    ret[k] = v(el)
                } else {
                    //in case of collection
                    ret[k] = v[index]
                }
            }
        }
        ret
    }
}
