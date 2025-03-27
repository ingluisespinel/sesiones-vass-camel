
package com.formadoresit.camel.springboot.soap;

import javax.xml.namespace.QName;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.formadoresit.camel.springboot.soap package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Hola_QNAME = new QName("http://soap.springboot.camel.formadoresit.com/", "hola");
    private final static QName _HolaResponse_QNAME = new QName("http://soap.springboot.camel.formadoresit.com/", "holaResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.formadoresit.camel.springboot.soap
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Hola }
     * 
     */
    public Hola createHola() {
        return new Hola();
    }

    /**
     * Create an instance of {@link HolaResponse }
     * 
     */
    public HolaResponse createHolaResponse() {
        return new HolaResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Hola }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Hola }{@code >}
     */
    @XmlElementDecl(namespace = "http://soap.springboot.camel.formadoresit.com/", name = "hola")
    public JAXBElement<Hola> createHola(Hola value) {
        return new JAXBElement<Hola>(_Hola_QNAME, Hola.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link HolaResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link HolaResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://soap.springboot.camel.formadoresit.com/", name = "holaResponse")
    public JAXBElement<HolaResponse> createHolaResponse(HolaResponse value) {
        return new JAXBElement<HolaResponse>(_HolaResponse_QNAME, HolaResponse.class, null, value);
    }

}
