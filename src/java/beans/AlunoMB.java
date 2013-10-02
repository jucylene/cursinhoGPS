/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.AlunoJpaController;
import dao.exceptions.NonexistentEntityException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.persistence.RollbackException;
import modelo.Aluno;
import util.EMF;
import util.FacesUtil;

/**
 *
 * @author ciro
 */
@ManagedBean
@RequestScoped
public class AlunoMB {
    private Aluno aluno = new Aluno();
    private AlunoJpaController dao = new AlunoJpaController(EMF.getEntityManagerFactory());
    
    /**
     * Carrega o Aluno clicado para este bean. Quando o usuário clica num
     * aluno numa tabela, invoca-se este método para caregá-lo neste bean.
     * @param aluno o Aluno que o usuário selecionou.
     */
    public void carregar(Aluno aluno){
        setAluno(aluno);
    }
    
    
    /**
     * Limpa o formulário de cadastro de alunos.
     * Ele apenas atribui um novo Aluno para este bean.
     */
    public void limpar(){
        setAluno(new Aluno());
    }
    
    public void excluir(Long id){
        try {
            dao.destroy(id);
            FacesUtil.adicionarMensagem("formCadAlunos", "O aluno foi excluído");
        } catch (NonexistentEntityException ex) {
            FacesUtil.adicionarMensagem("formCadAlunos", "Erro: O aluno não foi "
                    + "cadastrado ou já havia sido excluído");
            Logger.getLogger(AlunoMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void cadastrar(){
        try{
            if(verificarResponsavel()){
                dao.create(aluno);
                FacesUtil.adicionarMensagem("formCadAlunos", "O aluno foi cadastrado");
                aluno = new Aluno();
            } else {
                FacesUtil.adicionarMensagem("formCadAlunos", "Preencha os dados"
                        + " do seu responsável");
            }
        } catch (RollbackException e) {
            FacesUtil.adicionarMensagem("formCadAlunos", "Erro: Algo deu errado "
                    + "no cadastro");
        }
    }
    
    public void alterar(){
        try {
            if(aluno.getId() != null){
                dao.edit(aluno);
                aluno = new Aluno();
                FacesUtil.adicionarMensagem("formCadAlunos", "O aluno foi alterado");
            } else {
                FacesUtil.adicionarMensagem("formCadAlunos", "Nenhum aluno foi "
                        + "selecionado. Clique em um aluno para alterá-lo.");
            }
            
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(AlunoMB.class.getName()).log(Level.SEVERE, null, ex);
            FacesUtil.adicionarMensagem("formCadAlunos", "Erro: O aluno não foi "
                    + "cadastrado ou já havia sido excluído");
        } catch (Exception ex) {
            Logger.getLogger(AlunoMB.class.getName()).log(Level.SEVERE, null, ex);
            FacesUtil.adicionarMensagem("formCadAlunos", "Erro: Algo deu errado "
                    + "na alteração");
        }
    }
    
    /**
     * Verifica se o responsável dos alunos menores de idade está cadastrado.
     * Caso o aluno tenha menos de 18 anos e não informe o responsável, retorna
     * false. Caso tenha informado o responsável ou seja maior de 18 anos, retorna
     * true.
     */
    public boolean verificarResponsavel(){
        if (aluno.maioridade()){
            return true;
        } else if (!aluno.getResponsavel().isEmpty() && 
                   !aluno.getRespParentesco().isEmpty() &&
                   !aluno.getRespTelefone().isEmpty()) {
            // Menor de idade com responsável preenchido
            return true;
        }
        // Menor de idade sem responsável preenchido
        return false;
    }
    
    /**
     * Creates a new instance of AlunoMB
     */
    public AlunoMB() {
    }

    public Aluno getAluno() {
        return aluno;
    }

    /**
     * @param aluno the aluno to set
     */
    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }
    
    public List<Aluno> getAlunos(){
        return dao.findAlunoEntities();
    }
}
