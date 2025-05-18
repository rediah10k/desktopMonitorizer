package org.proy.monitorizerdesktop.clientserver.views.tablemodels;

import lombok.Getter;
import lombok.Setter;
import org.proy.monitorizerdesktop.clientserver.dtos.ConexionDTO;

import javax.swing.table.AbstractTableModel;
import java.util.List;


@Getter
@Setter
public class ConexionesActualesTableModel extends AbstractTableModel {
    private String[] columnNames={"IP","Puerto"};
    protected List<ConexionDTO> clientes;

    public ConexionesActualesTableModel(List<ConexionDTO> clientes){
        this.clientes = clientes;
    }
    @Override
    public int getRowCount() {
        return clientes.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
       ConexionDTO cliente = clientes.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> cliente.getIp();
            case 1 -> cliente.getPuerto();
            default -> null;
        };
    }
}
