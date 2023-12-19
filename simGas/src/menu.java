import com.mysql.jdbc.Statement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import koneksi.conek;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;



public class menu extends javax.swing.JFrame {

    /**
     * Creates new form menu
     */
    
    
    private Date date = new Date();
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/YY");
    Calendar cal = Calendar.getInstance();
    //private int kodeP = 0;
    private String kode;
    private boolean cekLogin;
    
    // deklarasi perusahaan
    private String tanggal2;
    private String barang2;
    private String kdb;
    private int jumlah2;
    private int harga2;
    private int hargaTotal2;
    
    // deklarasi analisis
    int stok_total = 0;
    int stok3kg = 0;
    int stok12kg = 0;
    int modal = 0;
    int TotalJual = 0;
    int jual3KG = 0;
    int jual12KG = 0;
    int masyarakat = 0;
    int umkm = 0;
    int pengecer = 0;
    int totalPelanggan = 0;
    int pelangganDekat = 0;
    int pelangganJauh = 0;
    int laba = 0;
    int hargaAwal = 0;
    String tempLaba = ""; // penjualan
    int labaSemuah = 0;
    
    private void reset(){
        stok_total = 0;
        stok3kg = 0;
        stok12kg = 0;
        modal = 0;
        TotalJual = 0;
        jual3KG = 0;
        jual12KG = 0;
        masyarakat = 0;
        umkm = 0;
        pengecer = 0;
        totalPelanggan = 0;
        pelangganDekat = 0;
        pelangganJauh = 0;
        String tempLaba = "";
        laba = 0;
        hargaAwal = 0;
        labaSemuah = 0;
    }
    
    
    public menu() {
        initComponents();
        this.setLocationRelativeTo(this);
        jPanelBgLogin.setVisible(false);
        jPanelPenjualanBG.setVisible(false);
        jPanelPerusahaan.setVisible(false);
        jPanelAnalisis.setVisible(false);
        jPanelPangkalan.setVisible(false);
        jPanelPelanggan.setVisible(false);
        cekLogin = false;
        analisis();
        
        jTextFieldKDP.setText(String.valueOf(maxPenjualan()+1));
        jTextFieldKDB.setText(String.valueOf(max()+1));
        
        datatable();
        datatablePelanggan();
        datatablePerusahaan();
        datatablePangkalan();
    }
    
    
    
    
    private int max(){
        int max = 0;
        
        try{
            Statement statement = (Statement)conek.getConnection().createStatement();
            ResultSet res = statement.executeQuery("select count(*) from t_perusahaan");
            
            res.next();
            max = res.getInt("count(*)");
            
        }catch (Exception e){
            JOptionPane.showMessageDialog(rootPane, "max salah");
        }
        
        return max;
    }
    
    private int maxPenjualan(){
        int max = 0;
        
        try{
            Statement statement = (Statement)conek.getConnection().createStatement();
            ResultSet res = statement.executeQuery("select count(*) from t_penjualan");
            
            res.next();
            max = res.getInt("count(*)");
            
        }catch (Exception e){
            JOptionPane.showMessageDialog(rootPane, "max salah");
        }
        
        return max;
    }
    
    private void analisis(){
        reset();
        
        int i=1;
        int maks = max();
        String tempBarang = "";
        String tempJumlah = "";
        String tempJenisP = "";
        String tempAsal = "";
        totalPelanggan = maxPenjualan();
        String tempLabah = "";
        
        while (i<=maks){
            
            try{
                Statement statement = (Statement)conek.getConnection().createStatement();
                ResultSet res = statement.executeQuery("select * from t_perusahaan where " 
                        + "kd_barang ='" + i+"'");

                
                while (res.next()){
                    stok_total+= Integer.parseInt(res.getString("jumlah"));
                    modal+= Integer.parseInt(res.getString("harga_total"));
                    tempBarang = res.getString("barang");
                    tempJumlah = res.getString("jumlah");
                }
            }catch (Exception e){
                JOptionPane.showMessageDialog(rootPane, "salah");
            }
            
                if(tempBarang.equals("LPG 3 KG")){
                    stok3kg+= Integer.parseInt(tempJumlah);
                }else{
                    stok12kg+= Integer.parseInt(tempJumlah);
                }
                i+=1;
        }
        
        
        i = 1;
        maks = maxPenjualan();
        while (i<=maks){
            
            try{
                Statement statement = (Statement)conek.getConnection().createStatement();
                ResultSet res = statement.executeQuery("select * from t_penjualan where " 
                        + "kd_pelanggan ='" + i+"'");

                while (res.next()){
                    tempBarang = res.getString("barang");
                    tempJumlah = res.getString("jumlah");
                    TotalJual += Integer.parseInt(tempJumlah);
                    tempJenisP = res.getString("jenis");
                    tempAsal = res.getString("asal");
                    tempLabah = res.getString("laba");
                    labaSemuah+= Integer.parseInt(String.valueOf(tempLabah));
                }
            }catch (Exception e){
                JOptionPane.showMessageDialog(rootPane, "salah");
            }
            
                if(tempBarang.equals("LPG 3 KG")){
                    jual3KG+= Integer.parseInt(tempJumlah);
                }else{
                    jual12KG+= Integer.parseInt(tempJumlah);
                }
                
                if(tempJenisP.equals("Masyarakat")){
                    masyarakat+=1;
                }else if(tempJenisP.equals("Pengecer")){
                    pengecer+=1;
                }else{
                    umkm+=1;
                }
                
                if(tempAsal.equals("< dari 1 KM")){
                    pelangganDekat+=1;
                }else{
                    pelangganJauh+=1;
                }
                
                i++;
        }
        
       
        
        
        jLabelTotalStok.setText(String.valueOf(stok_total - TotalJual));
        jLabelModal.setText(String.valueOf(modal));
        jLabelStok3.setText(String.valueOf(stok3kg - jual3KG));
        jLabelStok12.setText(String.valueOf(stok12kg - jual12KG));
        
        jLabelTotalJual.setText(String.valueOf(TotalJual));
        jLabelTotalJual3KG.setText(String.valueOf(jual3KG));
        jLabelTotalJual12KG.setText(String.valueOf(jual12KG));
        
        jLabelTotalPelanggan.setText(String.valueOf(totalPelanggan));
        jLabelMasyarakat.setText(String.valueOf((int)((double)masyarakat/totalPelanggan*100)) + "%");
        jLabelUMKM.setText(String.valueOf((int)((double)umkm/totalPelanggan*100)) + "%");
        jLabelPengecer.setText(String.valueOf((int)((double)pengecer/totalPelanggan*100)) + "%");
        
        jLabelPDekat.setText(String.valueOf((int)((double)pelangganDekat/totalPelanggan*100)) + "%");
        jLabelPJauh.setText(String.valueOf((int)((double)pelangganJauh/totalPelanggan*100)) + "%");
        
        
        jLabelLaba.setText(String.valueOf(labaSemuah));
        
        
        
                
        
    }
    

        public void datatable(){
       
        DefaultTableModel tbl = new DefaultTableModel();
        tbl.addColumn("Tanggal");
        tbl.addColumn("Kode");
        tbl.addColumn("Nama");
        tbl.addColumn("Asal");
        tbl.addColumn("Jenis");
        tbl.addColumn("Barang");
        tbl.addColumn("Jumlah");
        tbl.addColumn("Harga");
        tbl.addColumn("Total");
        tbl.addColumn("Laba");
        jTablePenjualan.setModel(tbl);
        
        try{
            Statement statement = (Statement)conek.getConnection().createStatement();
            ResultSet res = statement.executeQuery("select * from t_penjualan");
            while(res.next())
            {
                tbl.addRow(new Object[]{
                    res.getString("tanggal"),
                    res.getString("kd_pelanggan"),
                    res.getString("namap"),
                    res.getString("asal"),
                    res.getString("jenis"),
                    res.getString("barang"),
                    res.getString("jumlah"),
                    res.getString("harga"),
                    res.getString("harga_total"),
                    res.getString("laba")
                });
                jTablePenjualan.setModel(tbl);
            }
    }catch (Exception e){
            JOptionPane.showMessageDialog(rootPane,"salah");
    }
}
        
        public void datatablePelanggan(){
       
        DefaultTableModel tbl = new DefaultTableModel();
        tbl.addColumn("Kode");
        tbl.addColumn("Nama");
        tbl.addColumn("Keterangan");
        jTablePelanggan.setModel(tbl);
        
        try{
            Statement statement = (Statement)conek.getConnection().createStatement();
            ResultSet res = statement.executeQuery("select * from t_detail_pelanggan");
            while(res.next())
            {
                tbl.addRow(new Object[]{
                    res.getString("kd_pelanggan"),
                    res.getString("namap"),
                    res.getString("keterangan")
                });
                jTablePelanggan.setModel(tbl);
            }
    }catch (Exception e){
            JOptionPane.showMessageDialog(rootPane,"salah");
    }
}


        public void datatablePerusahaan(){
       
        DefaultTableModel tbl2 = new DefaultTableModel();
        tbl2.addColumn("Tanggal");
        tbl2.addColumn("Kode Barang");
        tbl2.addColumn("Barang");
        tbl2.addColumn("Jumlah");
        tbl2.addColumn("Harga");
        tbl2.addColumn("Harga Total");
        tbl2.addColumn("Status");
        jTablePerusahaan.setModel(tbl2);
        
        try{
            Statement statement = (Statement)conek.getConnection().createStatement();
            ResultSet res = statement.executeQuery("select * from t_perusahaan");
            while(res.next())
            {
                tbl2.addRow(new Object[]{
                    res.getString("tanggal"),
                    res.getString("kd_barang"),
                    res.getString("barang"),
                    res.getString("jumlah"),
                    res.getString("harga"),
                    res.getString("harga_total"),
                    res.getString("status")
                });
                jTablePerusahaan.setModel(tbl2);
            }
    }catch (Exception e){
            JOptionPane.showMessageDialog(rootPane,"salah");
    }
}
        
        public void datatablePangkalan(){
       
        DefaultTableModel tbl3 = new DefaultTableModel();
        tbl3.addColumn("Nama");
        tbl3.addColumn("Alamat");
        tbl3.addColumn("Telepon");
        jTableDataPang.setModel(tbl3);
        
        try{
            Statement statement = (Statement)conek.getConnection().createStatement();
            ResultSet res = statement.executeQuery("select * from t_pangkalan");
            while(res.next())
            {
                tbl3.addRow(new Object[]{
                    res.getString("namaPang"),
                    res.getString("alamatPang"),
                    res.getString("telpPang"),
                });
                jTableDataPang.setModel(tbl3);
            }
    }catch (Exception e){
            JOptionPane.showMessageDialog(rootPane,"salah");
    }
}
        
        
        
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanelPenjualanBG = new javax.swing.JPanel();
        jButtonPelanggan = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldNamaP = new javax.swing.JTextField();
        jTextFieldTgl = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jTextFieldKeterangan = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jComboBoxBarang = new javax.swing.JComboBox<>();
        jRadioButton2KM = new javax.swing.JRadioButton();
        jRadioButton1KM = new javax.swing.JRadioButton();
        jComboBoxJenisP = new javax.swing.JComboBox<>();
        jTextFieldJumlah = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jButtonCetakPj = new javax.swing.JButton();
        jButtonHome = new javax.swing.JButton();
        jButtonHapus = new javax.swing.JButton();
        jButtonSImpan = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jTextFieldKDP = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTablePenjualan = new javax.swing.JTable();
        jTextHarga = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabelPenjualanBg = new javax.swing.JLabel();
        jPanelPerusahaan = new javax.swing.JPanel();
        jButtonSimpan2 = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jTextFieldKDB = new javax.swing.JTextField();
        jComboBoxBarang2 = new javax.swing.JComboBox<>();
        jLabel16 = new javax.swing.JLabel();
        jTextFieldJumlahStok = new javax.swing.JTextField();
        jTextFieldHargaStok = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jTextFieldHargaTotal = new javax.swing.JTextField();
        jButtonHome3 = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        jTextFieldTanggal1 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTablePerusahaan = new javax.swing.JTable();
        jButtonHapus2 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabelBgPerusahaan = new javax.swing.JLabel();
        jPanelPangkalan = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jTextFieldNamaPang = new javax.swing.JTextField();
        jTextFieldAlamatPang = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jTextFieldNoPang = new javax.swing.JTextField();
        jButtonHapusPang = new javax.swing.JButton();
        jButtonKemPang = new javax.swing.JButton();
        jButtonSimpanPang = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableDataPang = new javax.swing.JTable();
        jPanelBg = new javax.swing.JPanel();
        jPanelBgLogin = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldUser = new javax.swing.JTextField();
        jButtonSubmitLogin = new javax.swing.JButton();
        jPasswordFieldPswd = new javax.swing.JPasswordField();
        jPanelLogout = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanelLogin = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabelBg1 = new javax.swing.JLabel();
        jLabelBg3 = new javax.swing.JLabel();
        jPanelLinkPangkalan = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jPanelLinkPerusahaan = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jPanelAN = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jPanelPJ = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jLabelBg2 = new javax.swing.JLabel();
        jPanelAnalisis = new javax.swing.JPanel();
        jLabelLaba = new javax.swing.JLabel();
        jLabelPJauh = new javax.swing.JLabel();
        jLabelPDekat = new javax.swing.JLabel();
        jLabelTotalPelanggan = new javax.swing.JLabel();
        jLabelPengecer = new javax.swing.JLabel();
        jLabelUMKM = new javax.swing.JLabel();
        jLabelMasyarakat = new javax.swing.JLabel();
        jLabelTotalJual12KG = new javax.swing.JLabel();
        jLabelTotalJual3KG = new javax.swing.JLabel();
        jLabelTotalJual = new javax.swing.JLabel();
        jLabelStok12 = new javax.swing.JLabel();
        jLabelStok3 = new javax.swing.JLabel();
        jLabelModal = new javax.swing.JLabel();
        jLabelTotalStok = new javax.swing.JLabel();
        jButtonHome4 = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        jLabelStok4 = new javax.swing.JLabel();
        jPanelPelanggan = new javax.swing.JPanel();
        jButtonHomePelanggan = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTablePelanggan = new javax.swing.JTable();
        jLabel28 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanelPenjualanBG.setBackground(new java.awt.Color(240, 245, 245));
        jPanelPenjualanBG.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButtonPelanggan.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jButtonPelanggan.setText("Pelanggan");
        jButtonPelanggan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPelangganActionPerformed(evt);
            }
        });
        jPanelPenjualanBG.add(jButtonPelanggan, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 370, 110, 30));

        jLabel5.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(99, 156, 196));
        jLabel5.setText("Nama Pelanggan");
        jPanelPenjualanBG.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, -1, -1));
        jPanelPenjualanBG.add(jTextFieldNamaP, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 120, 320, 30));

        jTextFieldTgl.setEditable(false);
        jPanelPenjualanBG.add(jTextFieldTgl, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 20, 140, 30));

        jLabel6.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(99, 156, 196));
        jLabel6.setText("Kode Pelanggan");
        jPanelPenjualanBG.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, -1, -1));

        jLabel7.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(99, 156, 196));
        jLabel7.setText("Jenis Pelanggan");
        jPanelPenjualanBG.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 220, -1, -1));

        jLabel8.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(99, 156, 196));
        jLabel8.setText("Asal");
        jPanelPenjualanBG.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, -1, -1));

        jLabel9.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(99, 156, 196));
        jLabel9.setText("Keterangan");
        jPanelPenjualanBG.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 320, -1, -1));
        jPanelPenjualanBG.add(jTextFieldKeterangan, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 320, 320, 30));

        jLabel10.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(99, 156, 196));
        jLabel10.setText("Harga");
        jPanelPenjualanBG.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 270, -1, 30));

        jComboBoxBarang.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jComboBoxBarang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "LPG 3 KG", "LPG 12 KG" }));
        jPanelPenjualanBG.add(jComboBoxBarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 270, 140, 30));

        buttonGroup1.add(jRadioButton2KM);
        jRadioButton2KM.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jRadioButton2KM.setText("> dari 1 KM");
        jPanelPenjualanBG.add(jRadioButton2KM, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 170, -1, -1));

        buttonGroup1.add(jRadioButton1KM);
        jRadioButton1KM.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jRadioButton1KM.setText("< dari 1 KM");
        jPanelPenjualanBG.add(jRadioButton1KM, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 170, -1, -1));

        jComboBoxJenisP.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jComboBoxJenisP.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Masyarakat", "Usaha Mikro", "Pengecer" }));
        jPanelPenjualanBG.add(jComboBoxJenisP, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 220, 140, 30));

        jTextFieldJumlah.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jTextFieldJumlah.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanelPenjualanBG.add(jTextFieldJumlah, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 220, 90, 30));

        jLabel11.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(99, 156, 196));
        jLabel11.setText("Barang");
        jPanelPenjualanBG.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, -1, -1));

        jButtonCetakPj.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jButtonCetakPj.setText("Cetak");
        jButtonCetakPj.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCetakPjActionPerformed(evt);
            }
        });
        jPanelPenjualanBG.add(jButtonCetakPj, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 370, 90, 30));

        jButtonHome.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jButtonHome.setText("Kembali");
        jButtonHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonHomeActionPerformed(evt);
            }
        });
        jPanelPenjualanBG.add(jButtonHome, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 20, 90, 30));

        jButtonHapus.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jButtonHapus.setText("Hapus");
        jButtonHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonHapusActionPerformed(evt);
            }
        });
        jPanelPenjualanBG.add(jButtonHapus, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 370, 90, 30));

        jButtonSImpan.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jButtonSImpan.setText("Simpan");
        jButtonSImpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSImpanActionPerformed(evt);
            }
        });
        jPanelPenjualanBG.add(jButtonSImpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 370, 100, 30));

        jLabel12.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(99, 156, 196));
        jLabel12.setText("Tanggal");
        jPanelPenjualanBG.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        jTextFieldKDP.setEditable(false);
        jTextFieldKDP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldKDPActionPerformed(evt);
            }
        });
        jPanelPenjualanBG.add(jTextFieldKDP, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 70, 140, 30));

        jTablePenjualan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTablePenjualan);

        jPanelPenjualanBG.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 430, 660, 90));
        jPanelPenjualanBG.add(jTextHarga, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 270, 90, 30));

        jLabel14.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(99, 156, 196));
        jLabel14.setText("Jumlah");
        jPanelPenjualanBG.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 220, -1, 30));

        jLabelPenjualanBg.setIcon(new javax.swing.ImageIcon("C:\\Users\\asus\\Pictures\\SIMGAS\\simGas\\src\\main.images\\BgPenjualan.png")); // NOI18N
        jPanelPenjualanBG.add(jLabelPenjualanBg, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 0, -1, -1));

        getContentPane().add(jPanelPenjualanBG, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 700, 540));

        jPanelPerusahaan.setBackground(new java.awt.Color(240, 245, 245));
        jPanelPerusahaan.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButtonSimpan2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jButtonSimpan2.setText("Simpan");
        jButtonSimpan2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSimpan2ActionPerformed(evt);
            }
        });
        jPanelPerusahaan.add(jButtonSimpan2, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 270, 80, 30));

        jLabel13.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(99, 156, 196));
        jLabel13.setText("Barang");
        jPanelPerusahaan.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, -1, 30));

        jLabel15.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(99, 156, 196));
        jLabel15.setText("Kode Barang");
        jPanelPerusahaan.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, -1, 30));

        jTextFieldKDB.setEditable(false);
        jTextFieldKDB.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanelPerusahaan.add(jTextFieldKDB, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 70, 160, 30));

        jComboBoxBarang2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jComboBoxBarang2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "LPG 3 KG", "LPG 12 KG" }));
        jPanelPerusahaan.add(jComboBoxBarang2, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 120, 260, 30));

        jLabel16.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(99, 156, 196));
        jLabel16.setText("Jumlah");
        jPanelPerusahaan.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, -1, 30));

        jTextFieldJumlahStok.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanelPerusahaan.add(jTextFieldJumlahStok, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 170, 260, 30));

        jTextFieldHargaStok.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanelPerusahaan.add(jTextFieldHargaStok, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 220, 260, 30));

        jLabel18.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(99, 156, 196));
        jLabel18.setText("Harga / Buah");
        jPanelPerusahaan.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 220, -1, 30));

        jLabel19.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(99, 156, 196));
        jLabel19.setText("Harga Total");
        jPanelPerusahaan.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 320, -1, 30));

        jTextFieldHargaTotal.setEditable(false);
        jTextFieldHargaTotal.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanelPerusahaan.add(jTextFieldHargaTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 320, 260, 30));

        jButtonHome3.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jButtonHome3.setText("Kembali");
        jButtonHome3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonHome3ActionPerformed(evt);
            }
        });
        jPanelPerusahaan.add(jButtonHome3, new org.netbeans.lib.awtextra.AbsoluteConstraints(599, 20, 80, 30));

        jLabel20.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(99, 156, 196));
        jLabel20.setText("Tanggal");
        jPanelPerusahaan.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, 30));

        jTextFieldTanggal1.setEditable(false);
        jTextFieldTanggal1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanelPerusahaan.add(jTextFieldTanggal1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, 160, 30));

        jTablePerusahaan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTablePerusahaan);

        jPanelPerusahaan.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 370, 650, 150));

        jButtonHapus2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jButtonHapus2.setText("Hapus");
        jButtonHapus2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonHapus2ActionPerformed(evt);
            }
        });
        jPanelPerusahaan.add(jButtonHapus2, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 270, 80, 30));

        jButton2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jButton2.setText("Cetak");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanelPerusahaan.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 270, 80, 30));

        jLabelBgPerusahaan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/main.images/BgPerusahaan.png"))); // NOI18N
        jPanelPerusahaan.add(jLabelBgPerusahaan, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, -30, 460, 580));

        getContentPane().add(jPanelPerusahaan, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 700, 540));

        jPanelPangkalan.setBackground(new java.awt.Color(240, 245, 245));
        jPanelPangkalan.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel17.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(99, 156, 196));
        jLabel17.setText("Nama Pangkalan");
        jPanelPangkalan.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 120, -1, -1));
        jPanelPangkalan.add(jTextFieldNamaPang, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 110, 210, 27));
        jPanelPangkalan.add(jTextFieldAlamatPang, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 160, 210, 27));

        jLabel26.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(99, 156, 196));
        jLabel26.setText("Alamat");
        jPanelPangkalan.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 160, -1, -1));

        jLabel27.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(99, 156, 196));
        jLabel27.setText("No Telepon");
        jPanelPangkalan.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 210, -1, -1));
        jPanelPangkalan.add(jTextFieldNoPang, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 210, 210, 27));

        jButtonHapusPang.setText("Hapus");
        jButtonHapusPang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonHapusPangActionPerformed(evt);
            }
        });
        jPanelPangkalan.add(jButtonHapusPang, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 260, 80, 30));

        jButtonKemPang.setText("Kembali");
        jButtonKemPang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonKemPangActionPerformed(evt);
            }
        });
        jPanelPangkalan.add(jButtonKemPang, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 20, 80, 27));

        jButtonSimpanPang.setText("Simpan");
        jButtonSimpanPang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSimpanPangActionPerformed(evt);
            }
        });
        jPanelPangkalan.add(jButtonSimpanPang, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 260, 80, 30));

        jTableDataPang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(jTableDataPang);

        jPanelPangkalan.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 320, 510, 58));

        getContentPane().add(jPanelPangkalan, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 700, 540));

        jPanelBg.setBackground(new java.awt.Color(240, 245, 245));
        jPanelBg.setPreferredSize(new java.awt.Dimension(718, 535));
        jPanelBg.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanelBgLogin.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Myriad Pro", 0, 17)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(99, 156, 196));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Password");
        jPanelBgLogin.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 10, -1, 30));

        jLabel2.setFont(new java.awt.Font("Myriad Pro", 0, 17)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(99, 156, 196));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Username");
        jPanelBgLogin.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 10, -1, 30));

        jTextFieldUser.setForeground(new java.awt.Color(99, 156, 196));
        jPanelBgLogin.add(jTextFieldUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 10, 110, 30));

        jButtonSubmitLogin.setText("Submit");
        jButtonSubmitLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSubmitLoginActionPerformed(evt);
            }
        });
        jPanelBgLogin.add(jButtonSubmitLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 10, 80, 30));

        jPasswordFieldPswd.setForeground(new java.awt.Color(99, 156, 196));
        jPanelBgLogin.add(jPasswordFieldPswd, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 10, 110, 30));

        jPanelBg.add(jPanelBgLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 700, 50));

        jPanelLogout.setBackground(new java.awt.Color(63, 183, 105));
        jPanelLogout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanelLogoutMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanelLogoutMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanelLogoutMousePressed(evt);
            }
        });
        jPanelLogout.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Myriad Pro", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("LOGOUT");
        jPanelLogout.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 5, 130, 20));

        jPanelBg.add(jPanelLogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 230, 130, 30));

        jPanelLogin.setBackground(new java.awt.Color(63, 183, 105));
        jPanelLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanelLoginMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanelLoginMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanelLoginMouseExited(evt);
            }
        });
        jPanelLogin.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Myriad Pro", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("LOGIN");
        jPanelLogin.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 5, 130, 20));

        jPanelBg.add(jPanelLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 230, 130, 30));

        jLabelBg1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/main.images/Bg1.png"))); // NOI18N
        jPanelBg.add(jLabelBg1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 700, -1));

        jLabelBg3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/main.images/Bg3.png"))); // NOI18N
        jPanelBg.add(jLabelBg3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 320, 700, 220));

        jPanelLinkPangkalan.setBackground(new java.awt.Color(97, 185, 193));
        jPanelLinkPangkalan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanelLinkPangkalanMousePressed(evt);
            }
        });

        jLabel25.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(240, 245, 245));
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setText("Pangkalan");

        javax.swing.GroupLayout jPanelLinkPangkalanLayout = new javax.swing.GroupLayout(jPanelLinkPangkalan);
        jPanelLinkPangkalan.setLayout(jPanelLinkPangkalanLayout);
        jPanelLinkPangkalanLayout.setHorizontalGroup(
            jPanelLinkPangkalanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLinkPangkalanLayout.createSequentialGroup()
                .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanelLinkPangkalanLayout.setVerticalGroup(
            jPanelLinkPangkalanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLinkPangkalanLayout.createSequentialGroup()
                .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanelBg.add(jPanelLinkPangkalan, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 390, 80, 20));

        jPanelLinkPerusahaan.setBackground(new java.awt.Color(97, 185, 193));
        jPanelLinkPerusahaan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanelLinkPerusahaanMousePressed(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(240, 245, 245));
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setText("Stok");

        javax.swing.GroupLayout jPanelLinkPerusahaanLayout = new javax.swing.GroupLayout(jPanelLinkPerusahaan);
        jPanelLinkPerusahaan.setLayout(jPanelLinkPerusahaanLayout);
        jPanelLinkPerusahaanLayout.setHorizontalGroup(
            jPanelLinkPerusahaanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLinkPerusahaanLayout.createSequentialGroup()
                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanelLinkPerusahaanLayout.setVerticalGroup(
            jPanelLinkPerusahaanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLinkPerusahaanLayout.createSequentialGroup()
                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanelBg.add(jPanelLinkPerusahaan, new org.netbeans.lib.awtextra.AbsoluteConstraints(395, 390, 90, 20));

        jPanelAN.setBackground(new java.awt.Color(97, 185, 193));
        jPanelAN.setPreferredSize(new java.awt.Dimension(56, 15));
        jPanelAN.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanelANMousePressed(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(240, 245, 245));
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setText("Analisis");

        javax.swing.GroupLayout jPanelANLayout = new javax.swing.GroupLayout(jPanelAN);
        jPanelAN.setLayout(jPanelANLayout);
        jPanelANLayout.setHorizontalGroup(
            jPanelANLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelANLayout.createSequentialGroup()
                .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanelANLayout.setVerticalGroup(
            jPanelANLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelANLayout.createSequentialGroup()
                .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanelBg.add(jPanelAN, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 390, 90, 20));

        jPanelPJ.setBackground(new java.awt.Color(97, 185, 193));
        jPanelPJ.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanelPJMousePressed(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(240, 245, 245));
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("Penjualan");

        javax.swing.GroupLayout jPanelPJLayout = new javax.swing.GroupLayout(jPanelPJ);
        jPanelPJ.setLayout(jPanelPJLayout);
        jPanelPJLayout.setHorizontalGroup(
            jPanelPJLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
        );
        jPanelPJLayout.setVerticalGroup(
            jPanelPJLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelPJLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanelBg.add(jPanelPJ, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 390, 88, 20));

        jLabelBg2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/main.images/Bg2Rev.png"))); // NOI18N
        jPanelBg.add(jLabelBg2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 390, -1, -1));

        getContentPane().add(jPanelBg, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 700, 540));

        jPanelAnalisis.setBackground(new java.awt.Color(240, 245, 245));
        jPanelAnalisis.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabelLaba.setFont(new java.awt.Font("Arial", 1, 26)); // NOI18N
        jLabelLaba.setForeground(new java.awt.Color(230, 231, 232));
        jLabelLaba.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelLaba.setText("0");
        jPanelAnalisis.add(jLabelLaba, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 420, 140, 40));

        jLabelPJauh.setFont(new java.awt.Font("Arial", 1, 22)); // NOI18N
        jLabelPJauh.setForeground(new java.awt.Color(97, 186, 193));
        jLabelPJauh.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelPJauh.setText("-");
        jPanelAnalisis.add(jLabelPJauh, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 450, 90, 50));

        jLabelPDekat.setFont(new java.awt.Font("Arial", 1, 22)); // NOI18N
        jLabelPDekat.setForeground(new java.awt.Color(97, 186, 193));
        jLabelPDekat.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelPDekat.setText("-");
        jPanelAnalisis.add(jLabelPDekat, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 370, 90, 50));

        jLabelTotalPelanggan.setFont(new java.awt.Font("Arial", 1, 26)); // NOI18N
        jLabelTotalPelanggan.setForeground(new java.awt.Color(230, 231, 232));
        jLabelTotalPelanggan.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelTotalPelanggan.setText("-");
        jPanelAnalisis.add(jLabelTotalPelanggan, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 400, 140, 100));

        jLabelPengecer.setFont(new java.awt.Font("Arial", 1, 22)); // NOI18N
        jLabelPengecer.setForeground(new java.awt.Color(230, 231, 232));
        jLabelPengecer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelPengecer.setText("-");
        jPanelAnalisis.add(jLabelPengecer, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 170, 90, 50));

        jLabelUMKM.setFont(new java.awt.Font("Arial", 1, 22)); // NOI18N
        jLabelUMKM.setForeground(new java.awt.Color(230, 231, 232));
        jLabelUMKM.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelUMKM.setText("-");
        jPanelAnalisis.add(jLabelUMKM, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 100, 90, 50));

        jLabelMasyarakat.setFont(new java.awt.Font("Arial", 1, 22)); // NOI18N
        jLabelMasyarakat.setForeground(new java.awt.Color(230, 231, 232));
        jLabelMasyarakat.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelMasyarakat.setText("-");
        jPanelAnalisis.add(jLabelMasyarakat, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 20, 90, 50));

        jLabelTotalJual12KG.setFont(new java.awt.Font("Arial", 1, 22)); // NOI18N
        jLabelTotalJual12KG.setForeground(new java.awt.Color(230, 231, 232));
        jLabelTotalJual12KG.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelTotalJual12KG.setText("-");
        jPanelAnalisis.add(jLabelTotalJual12KG, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 110, 80, 40));

        jLabelTotalJual3KG.setFont(new java.awt.Font("Arial", 1, 22)); // NOI18N
        jLabelTotalJual3KG.setForeground(new java.awt.Color(230, 231, 232));
        jLabelTotalJual3KG.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelTotalJual3KG.setText("-");
        jPanelAnalisis.add(jLabelTotalJual3KG, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 100, 90, 40));

        jLabelTotalJual.setFont(new java.awt.Font("Arial", 1, 26)); // NOI18N
        jLabelTotalJual.setForeground(new java.awt.Color(99, 156, 196));
        jLabelTotalJual.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelTotalJual.setText("-");
        jPanelAnalisis.add(jLabelTotalJual, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 200, 160, 40));

        jLabelStok12.setFont(new java.awt.Font("Arial", 1, 26)); // NOI18N
        jLabelStok12.setForeground(new java.awt.Color(240, 245, 245));
        jLabelStok12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelStok12.setText("-");
        jPanelAnalisis.add(jLabelStok12, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 190, 110, 40));

        jLabelStok3.setFont(new java.awt.Font("Arial", 1, 26)); // NOI18N
        jLabelStok3.setForeground(new java.awt.Color(230, 231, 232));
        jLabelStok3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelStok3.setText("-");
        jPanelAnalisis.add(jLabelStok3, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 80, 110, 40));

        jLabelModal.setFont(new java.awt.Font("Arial", 1, 26)); // NOI18N
        jLabelModal.setForeground(new java.awt.Color(230, 231, 232));
        jLabelModal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelModal.setText("-");
        jPanelAnalisis.add(jLabelModal, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 420, 140, 40));

        jLabelTotalStok.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        jLabelTotalStok.setForeground(new java.awt.Color(99, 156, 196));
        jLabelTotalStok.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelTotalStok.setText("-");
        jPanelAnalisis.add(jLabelTotalStok, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 150, 40));

        jButtonHome4.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jButtonHome4.setText("Kembali");
        jButtonHome4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonHome4ActionPerformed(evt);
            }
        });
        jPanelAnalisis.add(jButtonHome4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 90, 30));

        jLabel21.setIcon(new javax.swing.ImageIcon("C:\\Users\\asus\\Pictures\\SIMGAS\\simGas\\src\\main.images\\BgAnalisis.png")); // NOI18N
        jPanelAnalisis.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, -30, 730, 570));

        jLabelStok4.setFont(new java.awt.Font("Arial", 1, 26)); // NOI18N
        jLabelStok4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelStok4.setText("-");
        jPanelAnalisis.add(jLabelStok4, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 80, 110, 40));

        getContentPane().add(jPanelAnalisis, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 700, 540));

        jPanelPelanggan.setBackground(new java.awt.Color(240, 245, 245));

        jButtonHomePelanggan.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jButtonHomePelanggan.setText("Kembali");
        jButtonHomePelanggan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonHomePelangganActionPerformed(evt);
            }
        });

        jTablePelanggan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane4.setViewportView(jTablePelanggan);

        jLabel28.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel28.setText("Data Detail Pelanggan");

        javax.swing.GroupLayout jPanelPelangganLayout = new javax.swing.GroupLayout(jPanelPelanggan);
        jPanelPelanggan.setLayout(jPanelPelangganLayout);
        jPanelPelangganLayout.setHorizontalGroup(
            jPanelPelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPelangganLayout.createSequentialGroup()
                .addContainerGap(207, Short.MAX_VALUE)
                .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(113, 113, 113)
                .addComponent(jButtonHomePelanggan)
                .addGap(28, 28, 28))
            .addGroup(jPanelPelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelPelangganLayout.createSequentialGroup()
                    .addContainerGap(31, Short.MAX_VALUE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 644, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(25, Short.MAX_VALUE)))
        );
        jPanelPelangganLayout.setVerticalGroup(
            jPanelPelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPelangganLayout.createSequentialGroup()
                .addGroup(jPanelPelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelPelangganLayout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jButtonHomePelanggan))
                    .addGroup(jPanelPelangganLayout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(451, Short.MAX_VALUE))
            .addGroup(jPanelPelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelPelangganLayout.createSequentialGroup()
                    .addContainerGap(121, Short.MAX_VALUE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 384, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(35, Short.MAX_VALUE)))
        );

        getContentPane().add(jPanelPelanggan, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 700, 540));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private String username;
    private String pswd;
    
    private void loginReset(){
        jTextFieldUser.setText("");
        jPasswordFieldPswd.setText("");
        username = null;
        pswd = null;
    }
    
    public void setColorLogin(JPanel panel){
        panel.setBackground(new java.awt.Color(63,157,65));
    }
    
    public void resetColorLogin(JPanel panel){
        panel.setBackground(new java.awt.Color(63,183,105));
    }
    
    private void jPanelLoginMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelLoginMouseClicked
        jPanelBgLogin.setVisible(true);
    }//GEN-LAST:event_jPanelLoginMouseClicked

    private void jButtonSubmitLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSubmitLoginActionPerformed
        username = jTextFieldUser.getText();
        pswd = new String(jPasswordFieldPswd.getPassword());
        
        if (username.equals("admin") && pswd.equals("admin")){
            jPanelBgLogin.setVisible(false);
            loginReset();
            JOptionPane.showMessageDialog(this, " Selamat Datang Admin ! ");
            jLabelBg3.setVisible(false);
            cekLogin = true;
        }else{
            JOptionPane.showMessageDialog(this, " Username / Password Salah ! ");
            jPanelBgLogin.setVisible(false);
        }
    }//GEN-LAST:event_jButtonSubmitLoginActionPerformed

    private void jPanelLoginMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelLoginMouseEntered
        setColorLogin(jPanelLogin);
    }//GEN-LAST:event_jPanelLoginMouseEntered

    private void jPanelLoginMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelLoginMouseExited
        resetColorLogin(jPanelLogin);
    }//GEN-LAST:event_jPanelLoginMouseExited

    private void jPanelLogoutMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelLogoutMouseEntered
        setColorLogin(jPanelLogout);
    }//GEN-LAST:event_jPanelLogoutMouseEntered

    private void jPanelLogoutMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelLogoutMouseExited
        resetColorLogin(jPanelLogout);
    }//GEN-LAST:event_jPanelLogoutMouseExited

    private void jPanelLogoutMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelLogoutMousePressed
        JOptionPane.showMessageDialog(this, " Sampai Jumpa Admin ! ");
        jLabelBg3.setVisible(true);
        cekLogin = false;
    }//GEN-LAST:event_jPanelLogoutMousePressed

    private void jPanelPJMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelPJMousePressed
        if(cekLogin == true){
            jPanelBg.setVisible(false);
            jPanelPenjualanBG.setVisible(true);
        
            jTextFieldTgl.setText(String.valueOf(dateFormat.format(date)));
            analisis();
        }
    }//GEN-LAST:event_jPanelPJMousePressed

    private void jButtonHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonHomeActionPerformed
        jPanelPenjualanBG.setVisible(false);
        jPanelBg.setVisible(true);
    }//GEN-LAST:event_jButtonHomeActionPerformed

    private void prosesSimpanPJ(){

        jTextFieldTgl.setText(String.valueOf(dateFormat.format(date)));
        String barang = jComboBoxBarang.getSelectedItem().toString();
        int jumlahB = Integer.parseInt(jTextFieldJumlah.getText());
        String kodeP = jTextFieldKDP.getText();
        String tanggal = jTextFieldTgl.getText();
        String nama = jTextFieldNamaP.getText();
        String asal = "";
        
        if(jRadioButton1KM.isSelected()){
            asal = jRadioButton1KM.getText();
        }else if(jRadioButton2KM.isSelected()){
            asal = jRadioButton2KM.getText();
        }
        
        String jenisP = jComboBoxJenisP.getSelectedItem().toString();
        String keterangan = "" + jTextFieldKeterangan.getText();
        int harga = 0;
        int totalH = 0;
        
        
//        if(barang == "LPG 3 KG"){
//            harga = 30000;
//        }else{
//            harga = 100000;
//        }

        harga = Integer.parseInt(jTextHarga.getText());
        
        CariHargaAwal();
        
        totalH = harga*jumlahB;
        
       
        
        try{
            Statement statement = (Statement)conek.getConnection().createStatement();
            statement.executeUpdate("insert into t_penjualan VALUES ('" + tanggal + "','" + kodeP + "','" + nama + "','" + asal + 
                    "','" + jenisP + "','" + barang + "','" + keterangan + "','" + jumlahB + "','" + harga +  "','" + totalH + "','" + tempLaba + "');");
            statement.close();
            JOptionPane.showMessageDialog(null, "data berhasil disimpan");
        }catch (Exception t){
            JOptionPane.showMessageDialog(null, "daya gagal disimpan");
      }
        
        datatable();
        
         try{
            Statement statement = (Statement)conek.getConnection().createStatement();
            statement.executeUpdate("insert into t_detail_pelanggan VALUES ('" + kodeP + "','" + nama + "','" + keterangan + "');");
            statement.close();
            JOptionPane.showMessageDialog(null, "data berhasil disimpan");
        }catch (Exception t){
            JOptionPane.showMessageDialog(null, "daya gagal disimpan");
      }
        datatablePelanggan();
        
        
    }
    
    
    private void jButtonSImpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSImpanActionPerformed
        analisis();
        
        String barang = jComboBoxBarang.getSelectedItem().toString();
        int jumlahB = Integer.parseInt(jTextFieldJumlah.getText());
        
        if(barang.equals("LPG 3 KG") && jumlahB <= (stok3kg - jual3KG) ){
            stok3kg-= jumlahB;
            prosesSimpanPJ();
            jTextFieldKDP.setText(String.valueOf(maxPenjualan()+1));
        }else if(barang.equals("LPG 12 KG") && jumlahB <= (stok12kg - jual12KG)){
            stok12kg-= jumlahB;
            prosesSimpanPJ();
            jTextFieldKDP.setText(String.valueOf(maxPenjualan()+1));
        }else{
            if(barang.equals("LPG 3 KG")){
                JOptionPane.showMessageDialog(this, "Maaf tidak bisa karena jumlah LPG 3 KG yang akan dijual lebih "
                        + "besar dari stok yang ada");
            }else{
               if(barang.equals("LPG 3 KG")){
                JOptionPane.showMessageDialog(this, "Maaf tidak bisa karena jumlah LPG 12 KG yang akan dijual lebih "
                        + "besar dari stok yang ada"); 
                }
            }
        }
        
        analisis();
    }//GEN-LAST:event_jButtonSImpanActionPerformed

    private void jButtonCetakPjActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCetakPjActionPerformed
        String namaP = "";
        
        try{
                    Statement statement = (Statement)conek.getConnection().createStatement();
                    ResultSet res = statement.executeQuery("select * from t_pangkalan ");
                    

                while (res.next()){
                        namaP = res.getString("namaPang");
                        
                    }
                }catch (Exception e){
                    JOptionPane.showMessageDialog(rootPane, "salah");
        }
        
        if(namaP != ""){
            try {
                JasperPrint jp = JasperFillManager.fillReport(getClass().getResourceAsStream("reportPenjualan.jasper"), null, conek.getConnection());
                JasperViewer.viewReport(jp, false);
            }catch(Exception e) {
                JOptionPane.showMessageDialog(rootPane, e);
            }
        }else{
                JOptionPane.showMessageDialog(rootPane, "Untuk Cetak, Isi dahulu data pangkalan di menu");
        }
       
    }//GEN-LAST:event_jButtonCetakPjActionPerformed

    private void jButtonSimpan2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSimpan2ActionPerformed
        kdb = jTextFieldKDB.getText();
        tanggal2 = jTextFieldTanggal1.getText();
        barang2 = jComboBoxBarang2.getSelectedItem().toString();
        jumlah2 = Integer.parseInt(jTextFieldJumlahStok.getText());
        harga2 = Integer.parseInt(jTextFieldHargaStok.getText());
        hargaTotal2 = harga2*jumlah2;
        
        jTextFieldHargaTotal.setText(String.valueOf(hargaTotal2));
        
        try{
            Statement statement = (Statement)conek.getConnection().createStatement();
            statement.executeUpdate("insert into t_perusahaan VALUES ('" + tanggal2 + "','" + kdb + "','" + barang2 + "','" + jumlah2 + 
                    "','" + harga2 + "','" + hargaTotal2 + "','" + "Tersedia" + "');");
            statement.close();
            JOptionPane.showMessageDialog(null, "data berhasil disimpan");
        }catch (Exception t){
            JOptionPane.showMessageDialog(null, "daya gagal disimpan");
      }
        datatablePerusahaan();
        jTextFieldTanggal1.setText(String.valueOf(dateFormat.format(date)));
        jTextFieldKDB.setText(String.valueOf(max()+1));
        analisis();
    }//GEN-LAST:event_jButtonSimpan2ActionPerformed

    private void jPanelLinkPerusahaanMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelLinkPerusahaanMousePressed
        
        if(cekLogin == true){
            jPanelBg.setVisible(false);
            jPanelPerusahaan.setVisible(true);
        
            jTextFieldTanggal1.setText(String.valueOf(dateFormat.format(date)));
        }
    }//GEN-LAST:event_jPanelLinkPerusahaanMousePressed

    private void jButtonHome3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonHome3ActionPerformed
        jPanelPerusahaan.setVisible(false);
        jPanelBg.setVisible(true);
    }//GEN-LAST:event_jButtonHome3ActionPerformed

    private void jButtonHapus2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonHapus2ActionPerformed
        analisis();
        
        String kd = String.valueOf(max());
        int tempStok = 0;
        String tempBarang = "";
        boolean proses = false;
        
        try{
                    Statement statement = (Statement)conek.getConnection().createStatement();
                    ResultSet res = statement.executeQuery("select * from t_perusahaan where " 
                          + "kd_barang ='" + kd+ "'");
                    

                    while (res.next()){
                        tempStok = Integer.parseInt(res.getString("jumlah"));
                        tempBarang = res.getString("barang");
                        
                    }
                }catch (Exception e){
                    JOptionPane.showMessageDialog(rootPane, "salah");
                }
        
        if(tempBarang.equals("LPG 3 KG")){
            if((stok3kg-tempStok) >= jual3KG){
                proses = true;
                
            }else{
                JOptionPane.showMessageDialog(this, "Tidak bisa menghapus karena jika dihapus maka LPG 3 KG yang terjual akan l"
                        + "ebih kecil dari stok barang");
                JOptionPane.showMessageDialog(this, "Silahkan hapus terlebih dahulu LPG 3 KG terjual");
            }
        }else{
            if((stok12kg-tempStok) >= jual12KG){
                proses = true;
                
            }else{
                JOptionPane.showMessageDialog(this, "Tidak bisa menghapus karena jika dihapus maka LPG 12 KG yang terjual akan l"
                        + "ebih kecil dari stok barang");
                JOptionPane.showMessageDialog(this, "Silahkan hapus terlebih dahulu LPG 12 KG terjual");
            }
        }
        
        if(proses == true){
            try{
                Statement statement = (Statement)conek.getConnection().createStatement();
                statement.executeUpdate("DELETE from t_perusahaan where kd_barang=('" + kd + "');");
                JOptionPane.showMessageDialog(null, "data berhasil dihapus");  
            }catch (Exception t){
                JOptionPane.showMessageDialog(null, "data gagal dihapus");
            }
            datatablePerusahaan();   
            jTextFieldKDB.setText(String.valueOf(max()+1));
        }
        
    }//GEN-LAST:event_jButtonHapus2ActionPerformed

    private void jButtonHome4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonHome4ActionPerformed
        jPanelAnalisis.setVisible(false);
        jPanelBg.setVisible(true);
    }//GEN-LAST:event_jButtonHome4ActionPerformed

    private void jPanelANMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelANMousePressed
        if(cekLogin == true){
            jPanelBg.setVisible(false);
            jPanelAnalisis.setVisible(true);
        
            analisis();
        }
    }//GEN-LAST:event_jPanelANMousePressed

    private void jTextFieldKDPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldKDPActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldKDPActionPerformed

    private void jButtonHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonHapusActionPerformed
        analisis();
        
        String kd = String.valueOf(maxPenjualan());
        int j = 0;
        int stokB = 0;
        String tempBarang = "";
        int tempHapus = TotalJual;
        String status = "Habis";
        j = 1; 
        int tempMax = maxPenjualan();
        
        try{
                    Statement statement = (Statement)conek.getConnection().createStatement();
                    ResultSet res = statement.executeQuery("select * from t_penjualan where " 
                          + "kd_pelanggan ='" + kd+ "'");
                    

                    while (res.next()){
                        tempBarang = res.getString("barang");
                    }
                }catch (Exception e){
                    JOptionPane.showMessageDialog(rootPane, "salah");
                }

        try{
            Statement statement = (Statement)conek.getConnection().createStatement();
            statement.executeUpdate("DELETE from t_penjualan where kd_pelanggan=('" + kd + "');");
            JOptionPane.showMessageDialog(null, "data berhasil dihapus");
        }catch (Exception t){
            JOptionPane.showMessageDialog(null, "data gagal dihapus");
        }

        datatable();
        datatablePerusahaan();
        analisis();
        

        
        while(j<=max()){
            
            try{
                    Statement statement = (Statement)conek.getConnection().createStatement();
                    ResultSet res = statement.executeQuery("select * from t_perusahaan where " 
                          + "kd_barang ='" + j+ "' AND barang ='" + tempBarang + "'");
                    

                    while (res.next()){
                        stokB = Integer.parseInt(res.getString("jumlah"));
                        status = res.getString("status");
                        
                    }
                }catch (Exception e){
                    JOptionPane.showMessageDialog(rootPane, "salah");
                }
            
            
            tempHapus-=stokB;  
            
            if(maxPenjualan() == 0){
                
                try{
                            Statement statement = (Statement)conek.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                            ResultSet.CONCUR_UPDATABLE);
                            
                            statement.executeQuery("select * from t_perusahaan where " 
                            + "kd_barang ='" + j+ "' AND barang ='" + tempBarang + "'");
                            
                            ResultSet res = statement.getResultSet();
                            
                            while(res.next()){
                                res.updateString("status", "Tersedia");
                                res.updateRow();
                            }
                        }
                catch (Exception exc){
                                exc.printStackTrace();
                                JOptionPane.showMessageDialog(this, "salah lur");
                            }
                
            }else if(tempHapus <= 0){
                
                try{
                            Statement statement = (Statement)conek.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                            ResultSet.CONCUR_UPDATABLE);
                            
                            statement.executeQuery("select * from t_perusahaan where " 
                            + "kd_barang ='" + j+ "' AND barang ='" + tempBarang + "'");
                            
                            ResultSet res = statement.getResultSet();
                            
                            while(res.next()){
                                res.updateString("status", "Tersedia");
                                res.updateRow();
                            }
                        }
                catch (Exception exc){
                                exc.printStackTrace();
                                JOptionPane.showMessageDialog(this, "salah lur");
                            }
                
            }

             j++;
             datatable();
             datatablePerusahaan();
             analisis();
        
        }
        
        jTextFieldKDP.setText(String.valueOf(maxPenjualan()+1));
        analisis();
        datatablePelanggan();
    }//GEN-LAST:event_jButtonHapusActionPerformed

    
    
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        String namaP = "";
        
        try{
                    Statement statement = (Statement)conek.getConnection().createStatement();
                    ResultSet res = statement.executeQuery("select * from t_pangkalan ");
                    

                while (res.next()){
                        namaP = res.getString("namaPang");
                        
                    }
                }catch (Exception e){
                    JOptionPane.showMessageDialog(rootPane, "salah");
        }
        
        if(namaP != ""){
            try {
                JasperPrint jp = JasperFillManager.fillReport(getClass().getResourceAsStream("reportStok.jasper"), null, conek.getConnection());
                JasperViewer.viewReport(jp, false);
            }catch(Exception e) {
                JOptionPane.showMessageDialog(rootPane, e);
            }
        }else{
                JOptionPane.showMessageDialog(rootPane, "Untuk Cetak, Isi dahulu data pangkalan di menu");
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jPanelLinkPangkalanMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelLinkPangkalanMousePressed
        if(cekLogin == true){
            jPanelBg.setVisible(false);
            jPanelPangkalan.setVisible(true);
        }
    }//GEN-LAST:event_jPanelLinkPangkalanMousePressed

    private void jButtonKemPangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonKemPangActionPerformed
        jPanelPangkalan.setVisible(false);
        jPanelBg.setVisible(true);
    }//GEN-LAST:event_jButtonKemPangActionPerformed

    private void jButtonSimpanPangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSimpanPangActionPerformed
        
        datatablePangkalan();
        
        String namaP = "";
        String alamatP = "";
        String noP = "";
        
        
        try{
                    Statement statement = (Statement)conek.getConnection().createStatement();
                    ResultSet res = statement.executeQuery("select * from t_pangkalan ");
                    

                while (res.next()){
                        namaP = res.getString("namaPang");
                        
                    }
                }catch (Exception e){
                    JOptionPane.showMessageDialog(rootPane, "salah");
        }
        
       
        
        if(namaP == ""){
            
            namaP = jTextFieldNamaPang.getText();
            alamatP = jTextFieldAlamatPang.getText();
            noP = jTextFieldNoPang.getText();
            
            try{
                Statement statement = (Statement)conek.getConnection().createStatement();
                statement.executeUpdate("insert into t_pangkalan VALUES ('" + namaP + "','" + alamatP + "','" + noP + "');");
                statement.close();
                JOptionPane.showMessageDialog(null, "data berhasil disimpan");
            }catch (Exception t){
                JOptionPane.showMessageDialog(null, "daya gagal disimpan");
            }
            
            datatablePangkalan();
            
        }else{
            
                 JOptionPane.showMessageDialog(this, "Hapus dahulu nama pangkalan yang tersimpan!");
        }
        
    }//GEN-LAST:event_jButtonSimpanPangActionPerformed

    private void jButtonHapusPangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonHapusPangActionPerformed
        
        
        datatablePangkalan();
        
        String namaP = "";
        
        try{
                    Statement statement = (Statement)conek.getConnection().createStatement();
                    ResultSet res = statement.executeQuery("select * from t_pangkalan ");
                    

                while (res.next()){
                        namaP = res.getString("namaPang");
                        
                    }
                }catch (Exception e){
                    JOptionPane.showMessageDialog(rootPane, "salah");
        }
        
        if(namaP != "")
        
            try{
                Statement statement = (Statement)conek.getConnection().createStatement();
                statement.executeUpdate("DELETE from t_pangkalan where namaPang=('" + namaP + "');");
                JOptionPane.showMessageDialog(null, "data berhasil dihapus");
            }catch (Exception t){
                JOptionPane.showMessageDialog(null, "data gagal dihapus");
            }
        
        datatablePangkalan();
    }//GEN-LAST:event_jButtonHapusPangActionPerformed

    private void jButtonHomePelangganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonHomePelangganActionPerformed
        jPanelPelanggan.setVisible(false);
        jPanelPenjualanBG.setVisible(true);
    }//GEN-LAST:event_jButtonHomePelangganActionPerformed

    private void jButtonPelangganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPelangganActionPerformed
        jPanelPenjualanBG.setVisible(false);
        jPanelPelanggan.setVisible(true);
    }//GEN-LAST:event_jButtonPelangganActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new menu().setVisible(true);
            }
        });
    }
    
    
    
    private void CariHargaAwal(){
        analisis();
        
        String tempBarang = jComboBoxBarang.getSelectedItem().toString();
        int jB = Integer.parseInt(jTextFieldJumlah.getText());
        int i=1;
        int maks = max();
        int stokB = 0;
        int harga = Integer.parseInt(jTextHarga.getText());
        String status = "";
        
        int temp3KG = 0;
        int temp12KG = 0;
        int tempGas = 0;
        boolean cek = false;
        boolean done = false;
        
        if(tempBarang.equals("LPG 3 KG")){
            
            if(jual3KG == 0){
                tempGas = jB;
            }else{
                tempGas = (jual3KG + jB);
                cek = true;
            }
 
        }else{
            
            if(jual12KG == 0){
                tempGas = jB;
            }else{
                tempGas = (jual12KG + jB);
                cek = true;
            }
            
        }
        
        
        
        
            while(i<= maks){
                try{
                    Statement statement = (Statement)conek.getConnection().createStatement();
                    ResultSet res = statement.executeQuery("select * from t_perusahaan where " 
                          + "kd_barang ='" + i+ "' AND barang ='" + tempBarang + "'");
                    

                    while (res.next()){
                        hargaAwal = Integer.parseInt(res.getString("harga"));
                        stokB = Integer.parseInt(res.getString("jumlah"));
                        status = res.getString("status");
                        
                    }
                }catch (Exception e){
                    JOptionPane.showMessageDialog(rootPane, "salah");
                }
                
                if(tempBarang.equals("LPG 3 KG") && status.equals("Tersedia")){
                    
                    if(stokB > tempGas){
                        stokB = tempGas;
                        tempGas = stokB*-1;
                        done = true;
                    }else if(stokB == tempGas){
                        done = true;
                        tempGas = jB;
                       
                    }else{
                        tempGas-=stokB;
                    }
                    
                    
                    if(tempGas >= 0 ){
                        
                        try{
                                Statement statement = (Statement)conek.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                                ResultSet.CONCUR_UPDATABLE);
                                
                                statement.executeQuery("select * from t_perusahaan where " 
                                + "kd_barang ='" + i+ "' AND barang ='" + tempBarang + "'");
                                
                                ResultSet res = statement.getResultSet();
                                
                                while(res.next()){
                                    res.updateString("status", "Habis");
                                    res.updateRow();
                                }
                        }
                        catch (Exception exc){
                                    exc.printStackTrace();
                                    JOptionPane.showMessageDialog(this, "salah lur");
                                }
                        
                        
                        if(done == true){
                            
                            i=maks+1;
                    }
                        
                        if(cek == true){
                            
                            if(jB - tempGas == stokB){
                                //
                            }else if(jB <=  stokB){
                                stokB=jB;
                                i= maks+1;
                            }else{
                                stokB-=jB;
                                if(stokB < 0){
                                    stokB*=-1;
                                }
                            }
                            
                        }
                        
                        laba+= (harga-hargaAwal)*stokB;
                        
                        jB-=stokB;
                    }else{
                        if(done == true){
                            tempGas = jB*-1;
                        }
                        
                        laba+= (harga-hargaAwal)*(tempGas*-1);
                        i = maks+1;
                    }
                    
                    
                }else if(tempBarang.equals("LPG 3 KG") && status.equals("Habis")){
                    tempGas-=stokB;
                    
                    // 12KG
                }else if(tempBarang.equals("LPG 12 KG") && status.equals("Tersedia")){
                    
                    if(stokB > tempGas){
                        stokB = tempGas;
                        tempGas = stokB*-1;
                        done = true;
                    }else if(stokB == tempGas){
                        done = true;
                        tempGas = jB;
                       
                    }else{
                        tempGas-=stokB;
                    }
                    
                    
                    if(tempGas >= 0 ){
                        
                        try{
                                Statement statement = (Statement)conek.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                                ResultSet.CONCUR_UPDATABLE);
                                
                                statement.executeQuery("select * from t_perusahaan where " 
                                + "kd_barang ='" + i+ "' AND barang ='" + tempBarang + "'");
                                
                                ResultSet res = statement.getResultSet();
                                
                                while(res.next()){
                                    res.updateString("status", "Habis");
                                    res.updateRow();
                                }
                        }
                        catch (Exception exc){
                                    exc.printStackTrace();
                                    JOptionPane.showMessageDialog(this, "salah lur");
                                }
                        
                        
                        if(done == true){
                            
                            i=maks+1;
                    }
                        
                        if(cek == true){
                            
                            if(jB - tempGas == stokB){
                                //
                            }else if(jB <=  stokB){
                                stokB=jB;
                                i= maks+1;
                            }else{
                                stokB-=jB;
                                if(stokB < 0){
                                    stokB*=-1;
                                }
                            }
                            
                        }
                        
                        laba+= (harga-hargaAwal)*stokB;
                        
                        jB-=stokB;
                    }else{
                        if(done == true){
                            tempGas = jB*-1;
                        }
                        
                        laba+= (harga-hargaAwal)*(tempGas*-1);
                        i = maks+1;
                    }
                    
                    // 12KG
                }else if(tempBarang.equals("LPG 12 KG") && status.equals("Habis")){
                    tempGas-=stokB;
                }
                
                i++;
            }
       
            
            jLabelLaba.setText(String.valueOf(laba));
            tempLaba = String.valueOf(laba);
            datatable();
            datatablePerusahaan();
            analisis();
            
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButtonCetakPj;
    private javax.swing.JButton jButtonHapus;
    private javax.swing.JButton jButtonHapus2;
    private javax.swing.JButton jButtonHapusPang;
    private javax.swing.JButton jButtonHome;
    private javax.swing.JButton jButtonHome3;
    private javax.swing.JButton jButtonHome4;
    private javax.swing.JButton jButtonHomePelanggan;
    private javax.swing.JButton jButtonKemPang;
    private javax.swing.JButton jButtonPelanggan;
    private javax.swing.JButton jButtonSImpan;
    private javax.swing.JButton jButtonSimpan2;
    private javax.swing.JButton jButtonSimpanPang;
    private javax.swing.JButton jButtonSubmitLogin;
    private javax.swing.JComboBox<String> jComboBoxBarang;
    private javax.swing.JComboBox<String> jComboBoxBarang2;
    private javax.swing.JComboBox<String> jComboBoxJenisP;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelBg1;
    private javax.swing.JLabel jLabelBg2;
    private javax.swing.JLabel jLabelBg3;
    private javax.swing.JLabel jLabelBgPerusahaan;
    private javax.swing.JLabel jLabelLaba;
    private javax.swing.JLabel jLabelMasyarakat;
    private javax.swing.JLabel jLabelModal;
    private javax.swing.JLabel jLabelPDekat;
    private javax.swing.JLabel jLabelPJauh;
    private javax.swing.JLabel jLabelPengecer;
    private javax.swing.JLabel jLabelPenjualanBg;
    private javax.swing.JLabel jLabelStok12;
    private javax.swing.JLabel jLabelStok3;
    private javax.swing.JLabel jLabelStok4;
    private javax.swing.JLabel jLabelTotalJual;
    private javax.swing.JLabel jLabelTotalJual12KG;
    private javax.swing.JLabel jLabelTotalJual3KG;
    private javax.swing.JLabel jLabelTotalPelanggan;
    private javax.swing.JLabel jLabelTotalStok;
    private javax.swing.JLabel jLabelUMKM;
    private javax.swing.JPanel jPanelAN;
    private javax.swing.JPanel jPanelAnalisis;
    private javax.swing.JPanel jPanelBg;
    private javax.swing.JPanel jPanelBgLogin;
    private javax.swing.JPanel jPanelLinkPangkalan;
    private javax.swing.JPanel jPanelLinkPerusahaan;
    private javax.swing.JPanel jPanelLogin;
    private javax.swing.JPanel jPanelLogout;
    private javax.swing.JPanel jPanelPJ;
    private javax.swing.JPanel jPanelPangkalan;
    private javax.swing.JPanel jPanelPelanggan;
    private javax.swing.JPanel jPanelPenjualanBG;
    private javax.swing.JPanel jPanelPerusahaan;
    private javax.swing.JPasswordField jPasswordFieldPswd;
    private javax.swing.JRadioButton jRadioButton1KM;
    private javax.swing.JRadioButton jRadioButton2KM;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTableDataPang;
    private javax.swing.JTable jTablePelanggan;
    private javax.swing.JTable jTablePenjualan;
    private javax.swing.JTable jTablePerusahaan;
    private javax.swing.JTextField jTextFieldAlamatPang;
    private javax.swing.JTextField jTextFieldHargaStok;
    private javax.swing.JTextField jTextFieldHargaTotal;
    private javax.swing.JTextField jTextFieldJumlah;
    private javax.swing.JTextField jTextFieldJumlahStok;
    private javax.swing.JTextField jTextFieldKDB;
    private javax.swing.JTextField jTextFieldKDP;
    private javax.swing.JTextField jTextFieldKeterangan;
    private javax.swing.JTextField jTextFieldNamaP;
    private javax.swing.JTextField jTextFieldNamaPang;
    private javax.swing.JTextField jTextFieldNoPang;
    private javax.swing.JTextField jTextFieldTanggal1;
    private javax.swing.JTextField jTextFieldTgl;
    private javax.swing.JTextField jTextFieldUser;
    private javax.swing.JTextField jTextHarga;
    // End of variables declaration//GEN-END:variables
}
