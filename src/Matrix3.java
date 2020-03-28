class Matrix3 {
    double[] values;
    Matrix3(double[] values) {
        this.values = values;
    }
    Matrix3(){}

    Matrix3 multiply3(Matrix3 other) {
        double[] result = new double[9];
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                for (int i = 0; i < 3; i++) {
                    result[row * 3 + col] += this.values[row * 3 + i] * other.values[i * 3 + col];
                }
            }
        }
        return new Matrix3(result);
    }

    Matrix3 multiply4(Matrix3 other) {
        double[] result = new double[16];
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                for (int i = 0; i < 4; i++) {
                    result[row * 4 + col] += this.values[row * 4 + i] * other.values[i * 4 + col];
                }
            }
        }
        return new Matrix3(result);
    }
    Matrix3 make4from3(){
        double[] result = new double[16];
        int j = 0;
        for (int i = 0; i < 16 ; i ++){

            if (i == 15){
                result[i] = 1;
            }else if (i % 4 == 3){
                result[i] = 0;
            }else if (i > 11 && i < 15){
                result[i] = 0;
            }else{
                result[i] = this.values[j];
                j++;
            }
        }
        return new Matrix3(result);
    }


    Vertex transform(Vertex in) {
        return new Vertex(in.x * values[0] + in.y * values[3] + in.z * values[6],
                in.x * values[1] + in.y * values[4] + in.z * values[7],
                in.x * values[2] + in.y * values[5] + in.z * values[8]);
    }
    Vertex transform4(Vertex in) {
        return new Vertex(in.x * values[0] + in.y * values[1] + in.z * values[2] + values[3] ,
                in.x * values[4] + in.y * values[5] + in.z * values[6] + values[7],
                in.x * values[8] + in.y * values[9] + in.z * values[10] + values[11]);
    }
    Matrix3 getScaleMatrix(double scale){
        return new Matrix3(new double[]{
                    scale, 0, 0, 0,
                    0, scale, 0, 0,
                    0, 0, scale, 0,
                    0, 0, 0, 1});
    }
    Matrix3 getMoveMatrix(double[] xyz){
        return new Matrix3(new double[]{
                1, 0, 0, xyz[0],
                0, 1, 0, xyz[1],
                0, 0, 1, xyz[2],
                0, 0, 0, 1
        });
    }

}