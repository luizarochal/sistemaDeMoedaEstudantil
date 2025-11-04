package sistema.estudantil.system.dtos;

public class ResgateRequestDTO {
    private Long alunoId;
    private Long vantagemId;

    // Getters e Setters
    public Long getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(Long alunoId) {
        this.alunoId = alunoId;
    }

    public Long getVantagemId() {
        return vantagemId;
    }

    public void setVantagemId(Long vantagemId) {
        this.vantagemId = vantagemId;
    }
}