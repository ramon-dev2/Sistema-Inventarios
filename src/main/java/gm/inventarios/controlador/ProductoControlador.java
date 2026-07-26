package gm.inventarios.controlador;

import gm.inventarios.excepcion.RecursoNoEncontradoExcepcion;
import gm.inventarios.modelo.Producto;
import gm.inventarios.servicio.IProductoServicio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/productos")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductoControlador {

    private static final Logger logger =
            LoggerFactory.getLogger(ProductoControlador.class);

    private final IProductoServicio productoServicio;

    @Autowired
    public ProductoControlador(IProductoServicio productoServicio) {
        this.productoServicio = productoServicio;
    }

    @GetMapping
    public List<Producto> obtenerProductos() {
        List<Producto> productos = productoServicio.listarProductos();
        logger.info("Productos obtenidos:");
        productos.forEach(producto -> logger.info(producto.toString()));
        return productos;
    }

    @PostMapping
    public Producto agregarProducto(@RequestBody Producto producto) {
        logger.info("Producto a agregar: {}", producto);
        return productoServicio.guardarProducto(producto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProductoPorId(@PathVariable Integer id) {
        Producto producto = productoServicio.buscarProductoPorId(id);
        if (producto == null) {
            throw new RecursoNoEncontradoExcepcion(
                    "No se encontró el producto con id: " + id);
        }
        return ResponseEntity.ok(producto);
    }
}
