package sistema.estudantil.system.service;

import org.springframework.stereotype.Service;
import sistema.estudantil.system.models.Vantagem;
import sistema.estudantil.system.models.Aluno;

@Service
public class EmailService {

    public void enviarEmailNotificacaoMoeda(String emailAluno, String nomeProfessor, int quantidadeMoedas, String mensagem) {
        String assunto = "Você recebeu moedas!";
        String corpo = String.format(
            "Olá!\n\n" +
            "Você recebeu %d moedas do professor %s.\n" +
            "Mensagem: %s\n\n" +
            "Acesse o sistema para ver seu saldo atual.",
            quantidadeMoedas, nomeProfessor, mensagem
        );
        
        System.out.println("=== EMAIL ENVIADO ===");
        System.out.println("Para: " + emailAluno);
        System.out.println("Assunto: " + assunto);
        System.out.println("Corpo: " + corpo);
        System.out.println("=====================");
    }

    public void enviarEmailCupomAluno(String emailAluno, Vantagem vantagem, String codigoCupom) {
        String assunto = "Cupom de Vantagem - " + vantagem.getNome();
        String corpo = String.format(
            "Parabéns!\n\n" +
            "Você resgatou a vantagem: %s\n" +
            "Descrição: %s\n" +
            "Custo: %.2f moedas\n\n" +
            "CÓDIGO DO CUPOM: %s\n\n" +
            "Apresente este código no estabelecimento para usufruir da vantagem.",
            vantagem.getNome(), 
            vantagem.getDescricao(), 
            vantagem.getCusto(), 
            codigoCupom
        );
        
        System.out.println("=== EMAIL CUPOM ALUNO ===");
        System.out.println("Para: " + emailAluno);
        System.out.println("Assunto: " + assunto);
        System.out.println("Corpo: " + corpo);
        System.out.println("=========================");
    }

    public void enviarEmailNotificacaoParceiro(String emailEmpresa, Aluno aluno, Vantagem vantagem, String codigoCupom) {
        String assunto = "Novo resgate de vantagem - " + vantagem.getNome();
        String corpo = String.format(
            "Notificação de Resgate\n\n" +
            "O aluno %s resgatou a vantagem: %s\n" +
            "Email do aluno: %s\n" +
            "Curso: %s\n\n" +
            "CÓDIGO DO CUPOM PARA VALIDAÇÃO: %s\n\n" +
            "Por favor, valide este código quando o aluno apresentar.",
            aluno.getNome(), 
            vantagem.getNome(), 
            aluno.getEmail(), 
            aluno.getCurso(), 
            codigoCupom
        );
        
        System.out.println("=== EMAIL PARCEIRO ===");
        System.out.println("Para: " + emailEmpresa);
        System.out.println("Assunto: " + assunto);
        System.out.println("Corpo: " + corpo);
        System.out.println("======================");
    }
}