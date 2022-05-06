package com.emergentes.controlador;

import com.emergentes.dao.ProductoDAO;
import com.emergentes.dao.ProductoDAOimpl;
import com.emergentes.modelo.Producto;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ProductoControlador", urlPatterns = {"/ProductoControlador"})
public class ProductoControlador extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Producto pro = new Producto();
            int id;
            ProductoDAO dao = new ProductoDAOimpl();
            List<Producto> lista = null;

            String action = (request.getParameter("action") != null) ? request.getParameter("action") : "view";

            switch (action) {
                case "add":
                    request.setAttribute("producto", pro);
                    request.getRequestDispatcher("frmproducto.jsp").forward(request, response);
                    break;
                case "edit":
                    id = Integer.parseInt(request.getParameter("id"));
                    pro = dao.getById(id);
                    request.setAttribute("producto", pro);
                    request.getRequestDispatcher("frmproducto.jsp").forward(request, response);
                    break;
                case "delete":
                    id = Integer.parseInt(request.getParameter("id"));
                    dao.delete(id);
                    response.sendRedirect("ProductoControlador");
                    break;
                case "view":
                    // OBTENER LA LISTA DE REGISTROS
                    lista = dao.getAll();
                    request.setAttribute("productos", lista);
                    request.getRequestDispatcher("productos.jsp").forward(request, response);
                    break;
            }

        } catch (Exception ex) {
            System.out.println("ERROR AL " + ex.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        float precio = Float.parseFloat(request.getParameter("precio"));

        Producto pro = new Producto();

        pro.setId(id);
        pro.setNombre(nombre);
        pro.setDescripcion(descripcion);
        pro.setPrecio(precio);

        if (id == 0) {
            ProductoDAO dao = new ProductoDAOimpl();
            try {
                // NUEVO REGISTRO
                dao.insert(pro);
                response.sendRedirect("ProductoControlador");

            } catch (Exception ex) {
                System.out.println("ERROR AL INTRODUCIR UN NUEVO REGISRO" + ex.getMessage());
            }
        } else {
            ProductoDAO dao = new ProductoDAOimpl();
            try {
                // EDICION DE REGISTRO
                dao.update(pro);
                response.sendRedirect("ProductoControlador");

            } catch (Exception ex) {
                System.out.println("ERROR AL EDITAR EL REGISTRO" + ex.getMessage());
            }
        }
        response.sendRedirect("ClienteControlador");
    }

}
