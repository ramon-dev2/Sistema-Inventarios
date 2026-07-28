package gm.inventarios.controlador;

import gm.inventarios.excepcion.RecursoNoEncontradoExcepcion;
import gm.inventarios.modelo.Producto;
import gm.inventarios.servicio.IProductoServicio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(
            @PathVariable Integer id,
            @RequestBody Producto productoRecibido) {
        Producto producto = productoServicio.buscarProductoPorId(id);
        if (producto == null) {
            throw new RecursoNoEncontradoExcepcion(
                    "No se encontró el producto con id: " + id);
        }

        producto.setDescripcion(productoRecibido.getDescripcion());
        producto.setPrecio(productoRecibido.getPrecio());
        producto.setExistencia(productoRecibido.getExistencia());
        if (productoRecibido.getActivo() != null) {
            producto.setActivo(productoRecibido.getActivo());
        }

        Producto productoActualizado = productoServicio.guardarProducto(producto);
        logger.info("Producto actualizado: {}", productoActualizado);
        return ResponseEntity.ok(productoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> eliminarProducto(
            @PathVariable Integer id) {
        Producto producto = productoServicio.buscarProductoPorId(id);
        if (producto == null) {
            throw new RecursoNoEncontradoExcepcion(
                    "No se encontró el producto con id: " + id);
        }

        productoServicio.eliminarProductoPorId(producto.getIdProducto());
        logger.info("Producto eliminado con id: {}", id);

        Map<String, Boolean> respuesta = new HashMap<>();
        respuesta.put("eliminado", Boolean.TRUE);
        return ResponseEntity.ok(respuesta);
    }
}
