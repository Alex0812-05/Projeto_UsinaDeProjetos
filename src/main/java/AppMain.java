 import javax.swing.*; 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AppMain extends JFrame {
    private int pontos = 0;
    private Connection conn;
    
    public AppMain() {
        setTitle("Aplicativo de Coleta Seletiva");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Conectar ao banco de dados
        conectarBancoDeDados();

        // Painel principal
        JPanel panel = new JPanel();
        panel.setLayout(null);

        // Botão para acessar Educação Ambiental
        JButton btnEducacao = new JButton("Educação Ambiental");
        btnEducacao.setBounds(100, 30, 300, 30);
        panel.add(btnEducacao);
        btnEducacao.addActionListener(e -> mostrarEducacaoAmbiental());

        // Botão para acessar Pontos de Coleta
        JButton btnPontosDeColeta = new JButton("Pontos de Coleta");
        btnPontosDeColeta.setBounds(100, 80, 300, 30);
        panel.add(btnPontosDeColeta);
        btnPontosDeColeta.addActionListener(e -> mostrarPontosDeColeta());

        // Botão para Descarte de Lixo
        JButton btnDescarte = new JButton("Descarte de Lixo");
        btnDescarte.setBounds(100, 130, 300, 30);
        panel.add(btnDescarte);
        btnDescarte.addActionListener(e -> mostrarDescarteDeLixo());

        // Botão para visualizar Relatório
        JButton btnRelatorio = new JButton("Relatório Estatístico");
        btnRelatorio.setBounds(100, 180, 300, 30);
        panel.add(btnRelatorio);
        btnRelatorio.addActionListener(e -> mostrarRelatorio());

        // Botão para ver Pontuação
        JButton btnPontuacao = new JButton("Ver Pontuação");
        btnPontuacao.setBounds(100, 230, 300, 30);
        panel.add(btnPontuacao);
        btnPontuacao.addActionListener(e -> verPontuacao());

        add(panel);
        setVisible(true);
    }

    // Conexão com o banco de dados MySQL
    private void conectarBancoDeDados() {
        try {
            // Conexão com o MySQL
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/coleta_seletiva", "root", "");
            System.out.println("Conexão com o banco de dados bem-sucedida!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco de dados: " + e.getMessage());
        }
    }

    // Método para exibir informações de Educação Ambiental
    private void mostrarEducacaoAmbiental() {
        JOptionPane.showMessageDialog(this,
                "Tipos de Lixo:\n1. Orgânico\n2. Reciclável\n3. Eletrônico\n4. Perigoso\n\n" +
                        "Dicas:\n- Separe o lixo corretamente.\n- Reduza o uso de plásticos.");
    }

    // Método para mostrar Pontos de Coleta
    private void mostrarPontosDeColeta() {
        JOptionPane.showMessageDialog(this, "Ponto de Coleta: Biblioteca\nTipo de Lixo: Reciclável\n\n" +
                "Ponto de Coleta: Bloco A\nTipo de Lixo: Orgânico");
    }

    // Método para Descarte de Lixo usando JComboBox (Seleção)
    private void mostrarDescarteDeLixo() {
        String[] tiposLixo = {"Reciclável - Plástico", "Reciclável - Papel", "Orgânico", "Eletrônico", "Perigoso"};
        JComboBox<String> tipoLixoBox = new JComboBox<>(tiposLixo);
        tipoLixoBox.setBounds(100, 130, 300, 30);
        
        // Exibe o painel com o combo box
        JPanel painelSelecao = new JPanel();
        painelSelecao.add(tipoLixoBox);
        int option = JOptionPane.showOptionDialog(this, painelSelecao, "Selecione o Tipo de Descarte",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);

        if (option == JOptionPane.OK_OPTION) {
            String tipoLixoSelecionado = (String) tipoLixoBox.getSelectedItem();
            String tipoResiduo = "Residuo desconhecido.";
            
            switch (tipoLixoSelecionado) {
                case "Reciclável - Plástico":
                    tipoResiduo = "Plástico";
                    adicionarPontos(10);
                    break;
                case "Reciclável - Papel":
                    tipoResiduo = "Papel";
                    adicionarPontos(10);
                    break;
                case "Orgânico":
                    tipoResiduo = "Orgânico";
                    adicionarPontos(5);
                    break;
                case "Eletrônico":
                    tipoResiduo = "Eletrônico";
                    adicionarPontos(15);
                    break;
                case "Perigoso":
                    tipoResiduo = "Perigoso";
                    adicionarPontos(20);
                    break;
            }
            // Salva no banco de dados
            salvarDescarte(tipoResiduo);
            JOptionPane.showMessageDialog(this, "Residuo Classificado como: " + tipoResiduo);
        }
    }

    // Método para salvar o descarte no banco de dados
    private void salvarDescarte(String tipoResiduo) {
        try {
            String sql = "INSERT INTO descarte (tipo_residuo, pontos) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, tipoResiduo);
            pstmt.setInt(2, pontos);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar descarte no banco de dados: " + e.getMessage());
        }
    }

    // Método para adicionar pontos de incentivo
    private void adicionarPontos(int valor) {
        pontos += valor;
        JOptionPane.showMessageDialog(this, "Pontos adicionados! Total de pontos: " + pontos);
    }

    // Método para ver pontuação
    private void verPontuacao() {
        JOptionPane.showMessageDialog(this, "Pontuação atual: " + pontos);
    }

    // Método para exibir Relatório Estatístico
    private void mostrarRelatorio() {
        JOptionPane.showMessageDialog(this, "Relatório de Resíduos Coletados:\n" +
                "- Orgânico: 50kg\n- Reciclável: 120kg\n- Eletrônico: 30kg");
    }

    public static void main(String[] args) {
        new AppMain();
    }
}