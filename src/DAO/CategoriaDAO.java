/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package DAO;

import Model.Categoria;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CategoriaDAO{

    /**
     *
     * @author arthu
     */

    /**
         * @return 
     */
 public static Connection conectar() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/db_estoque", "root", "root123"
            );
            return con;

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(CategoriaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

        //Criação
        public boolean insertCategoria (Categoria categoria){
        String sql = "INSERT INTO tb_categorias (nome, tamanho, embalagem) VALUES (?, ?, ?)";
        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, categoria.getNome());
            stmt.setString(2, categoria.getTamanho());
            stmt.setString(3, categoria.getEmbalagem());

            stmt.executeUpdate();  
            stmt.close();

            return true;

        } catch (SQLException e) {
            System.out.println("Erro:" + e);
            throw new RuntimeException(e);
        }
    }

        //Ler
        public ArrayList<Categoria> ListaCategorias = new ArrayList<>();
        public ArrayList<Categoria> getListaCategorias(){
            ListaCategorias.clear();
            String sql = "SELECT * FROM tb_categorias";
            try(Connection conn = conectar();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)){
                    while (rs.next()){
                        int id = rs.getInt("id_categoria");
                        String nome = rs.getString("nome");
                        String tamanho = rs.getString("tamanho");
                        String embalagem = rs.getString("embalagem");

                        Categoria categoria = new Categoria (id, nome, tamanho, embalagem);

                        ListaCategorias.add(categoria);
                    }
                    stmt.close();
                }catch (SQLException e){
                    System.out.println("Erro:" + e);
                }
            return ListaCategorias;
        }
        public void setListaCategorias(ArrayList<Categoria> ListaCategorias){
            this.ListaCategorias = ListaCategorias;
        }

        //Update
        public boolean updateCategoria(Categoria categoria){
            String sql = "UPDATE tb_categorias set nome = ?, tamanho = ?, embalagem = ? WHERE id_categoria = ?";
            try (Connection conn = conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)){

                stmt.setString(1, categoria.getNome());
                stmt.setString(2, categoria.getTamanho());
                stmt.setString(3, categoria.getEmbalagem());
                stmt.setInt(4, categoria.getId());

                stmt.execute();
                stmt.close();
                return true;

            } catch (SQLException e) {
                System.out.println("Erro:" + e);
                throw new RuntimeException(e);
            }
        }

        //Remove
        public boolean deleteCategoria(Categoria categoria){
            String sql = ("DELETE from tb_categorias WHERE id_categoria = ? ");
            try (Connection conn = conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)){

                    stmt.setInt(1, categoria.getId());
                    stmt.executeUpdate();
                    return true;

                } catch (SQLException e){
                    System.out.println("Erro" + e);
                    throw new RuntimeException(e);
                }
        }

    }



