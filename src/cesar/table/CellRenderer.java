package cesar.table;

import cesar.util.Bytes;
import cesar.util.Shorts;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

public final class CellRenderer {
    private CellRenderer() {
    }

    protected static class CenteredTableCellRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 5810811798031094957L;

        public CenteredTableCellRenderer() {
            super();
            setHorizontalAlignment(JLabel.CENTER);
        }
    }

    protected static class DecimalByteRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = -8860753344065220474L;

        public DecimalByteRenderer() {
            super();
            setHorizontalAlignment(JLabel.RIGHT);
        }

        @Override
        public void setValue(Object value) {
            assert value instanceof Byte;
            setText(Integer.toString(Bytes.toUnsignedInt((byte) value)));
        }
    }

    protected static class HexadecimalByteRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 5810811798031094957L;

        public HexadecimalByteRenderer() {
            super();
            setHorizontalAlignment(JLabel.RIGHT);
        }

        @Override
        public void setValue(Object value) {
            assert value instanceof Byte;
            setText(Integer.toHexString(Bytes.toUnsignedInt((Byte) value)));
        }
    }

    protected static class DecimalShortRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = -8860753344065220474L;

        public DecimalShortRenderer() {
            super();
            setHorizontalAlignment(JLabel.RIGHT);
        }

        @Override
        public void setValue(Object value) {
            assert value instanceof Short;
            setText(Integer.toString(Shorts.toUnsignedInt((short) value)));
        }
    }

    protected static class HexadecimalShortRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 5810811798031094957L;

        public HexadecimalShortRenderer() {
            super();
            setHorizontalAlignment(JLabel.RIGHT);
        }

        @Override
        public void setValue(Object value) {
            assert value instanceof Short;
            setText(Integer.toHexString(Shorts.toUnsignedInt((Short) value)));
        }
    }
}
