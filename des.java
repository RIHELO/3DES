/*
 * des.java
 *
 * Created on March 10, 2003, 6:41 PM
 */
/***********************************
*    DATA ENCRYPTION STANDARD
*    3DES 3key CBC Mode
*    Version 1.0
*    RICARDO HERNANDEZ LOPEZ
************************************/

/**
 *
 * @author  Ricardo Hernandez
 */
public class des {
    
    /** Creates a new instance of des */
    public des() {
    }
    
/*
#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <time.h>
#include <math.h> 
#define  BIG_BLOCK  1000000
#define  BLOCK_SIZE 8
#define  KEY_SIZE   64
#define  EXPANDED   48
#define  COMPRESSED 32
 */
static long  BIG_BLOCK  =1000000;
static int   BLOCK_SIZE =8;
static int   KEY_SIZE   =64;
static int   EXPANDED   =48;
static int   COMPRESSED =32;    
boolean      DEBUG= false;
int      ifile,ofile,kfile,kfile2,kfile3,cont;
int      size;
int      G=0;
 byte K1[] =new byte [KEY_SIZE];
 byte K2[] =new byte [KEY_SIZE];
 byte K3[] =new byte [KEY_SIZE];
 byte IK1[]=new byte [KEY_SIZE];
 byte IV[] =new byte [KEY_SIZE];

public void Debug(String text)
{
  if(DEBUG)
    System.out.println("%s"+text);
}
/*
public void openfiles(String file1,String file2,String file3,String file4,String file5)
{
  System.out.println("Openfiles\n");
  if(( ifile = open( file1, O_RDONLY ))==-1)
    {
      System.out.println("ERROR: cannot open file > %s\n"+file1);
      System.exit(1);
    }
    else
      System.out.println("Input    : %s\n"+file1);
  if(( ofile = open( file2, O_WRONLY | O_CREAT | O_TRUNC ))==-1)
    {
      System.out.println("ERROR: cannot open file > %s\n"+file2);
      System.exit(1);
    }
    else
      System.out.println("Output   : %s\n"+file2);
   if(( kfile = open( file3, O_RDONLY ))==-1)
    {
      System.out.println("ERROR: cannot open file > %s\n"+file3);
      System.exit(1);
    }
    else
      System.out.println("Key file : %s\n"+file3);
 if(( kfile2 = open( file4, O_RDONLY ))==-1)
    {
      System.out.println("ERROR: cannot open file > %s\n"+file4);
      System.exit(1);
    }
    else
      System.out.println("Key file : %s\n"+file4);
 if(( kfile3 = open( file5, O_RDONLY ))==-1)
    {
      System.out.println("ERROR: cannot open file > %s\n"+file5);
      System.exit(1);
    }
    else
      System.out.println("Key file : %s\n"+file5);
}

public void closefiles(String file1,String file2,String file3,String file4,String file5)
{ 
  int i,j,k,l,m;
  if( (i=close(ifile)) == -1)
    {
      System.out.println("ERROR: cannot close file %s\n"+file1);
      System.exit(1);
    }
  if( (j=close(ofile)) == -1)
    {
      System.out.println("Error: cannot close file %s\n"+file2);
      System.exit(1);
    }
  if( (k=close(kfile)) == -1)
    {
      System.out.println("Error: cannot close file %s\n"+file2);
      System.exit(1);
    }
  if( (l=close(kfile2)) == -1)
    {
      System.out.println("Error: cannot close file %s\n"+file2);
      System.exit(1);
    }
  if( (m=close(kfile3)) == -1)
    {
      System.out.println("Error: cannot close file %s\n"+file2);
      System.exit(1);
    }
}

int readfile(int handle,long maxval, byte[] Buffer)
{
  size = read(handle,Buffer,maxval);
  if(DEBUG)
  System.out.println("size read: %d \n"+size);
  return size;
}
/**/
public void transform( byte [] key)
{
  int i;
 for( i=0 ; i < KEY_SIZE ; i++ )
    {    
     int tmp = key[i] & 0x01;
     key[i]=(byte)tmp;
    }
}

public void tobinary( byte[]  line,int num, byte[]  out)
{
  int i,j,k=0;
   int tmp=0;
  Debug("to binary\n");
  for ( i=0 ; i < num ; i++)
    {
       tmp = line[i];
       for( j=7 ; j >= 0 ; j--)
       {
         //out[k+j] = 
         int t= (int)tmp & 0x01;
         out[k+j] =(byte)t;
	 tmp=tmp>>1;
         
       }
       k+=8;
    }
  if(DEBUG)
    {
      for( i=0 ; i < num*8 ; i++ )
  	System.out.println("%x"+out[i]);
      System.out.println("\n");
    }
}

public void Copy( byte[] a, byte [] b,int num)
{
  int i;
  Debug("Copy\n");
   for(i = 0 ; i < num ; i++ )
    b[i] = a[i];
  if(DEBUG)
    {
      for( i=0 ; i < num ; i++)
        System.out.println("%x"+b[i]);
      System.out.println("\n"); 
    }
}

byte[] IP={
  58 , 50 , 42 , 34 , 26 , 18 , 10 , 2,
  60 , 52 , 44 , 36 , 28 , 20 , 12 , 4,
  62 , 54 , 46 , 38 , 30 , 22 , 14 , 6,
  64 , 56 , 48 , 40 , 32 , 24 , 16 , 8,
  57 , 49 , 41 , 33 , 25 , 17 , 9  , 1,
  59 , 51 , 43 , 35 , 27 , 19 , 11 , 3,
  61 , 53 , 45 , 37 , 29 , 21 , 13 , 5,
  63 , 55 , 47 , 39 , 31 , 23 , 15 , 7
};

public void IniPermutation( byte  [] a,byte [] out)
{
  int i;
  Debug("InitialPermutation\n");
   for( i=0 ; i < 64 ; i++ )
      out[ IP[i]-1 ]=a[i];
   if(DEBUG)
     {
       for( i=0 ; i < 64 ; i++ )
	 System.out.println("%x"+out[i]);
       System.out.println("\n");
     }
}

byte[] PC1={
  57 , 49 , 41 , 33 , 25 , 17 , 9  ,
  1  , 58 , 50 , 42 , 34 , 26 , 18 ,
  10 , 2  , 59 , 51 , 43 , 35 , 27 ,
  19 , 11 , 3  , 60 , 52 , 44 , 36 ,
  63 , 55 , 47 , 39 , 31 , 23 , 15 , 
  7  , 62 , 54 , 46 , 38 , 30 , 22 ,
  14 , 6  , 61 , 53 , 45 , 37 , 29 ,
  21 , 13 , 5  , 28 , 20 , 12 , 4
};

public void Permutation1( byte [] initialkey, byte [] out)
{
  int i;
  Debug("Permutation Choice 1 (KEY)\n");
  if(DEBUG)
   {
     for(i = 0 ; i < 64 ; i++ )
         System.out.println("%x"+initialkey[i]);
     System.out.println("\n");
   }
  for(i = 0 ; i < 64 ; i++ )
         out[i]=0;
  for( i=0 ; i < 56 ; i++ )
     out[ i ] = initialkey[ PC1[i]-1 ];
  if(DEBUG)
   {
     for(i=0 ; i < 56 ; i++ )
        System.out.println("%x"+out[i]);
     System.out.println("\n");
   }
}

byte[] XP={
  32 , 1  , 2  , 3  , 4  , 5 ,
  4  , 5  , 6  , 7  , 8  , 9 ,
  8  , 9  , 10 , 11 , 12 , 13 ,
  12 , 13 , 14 , 15 , 16 , 17 ,
  16 , 17 , 18 , 19 , 20 , 21 ,
  20 , 21 , 22 , 23 , 24 , 25 ,
  24 , 25 , 26 , 27 , 28 , 29 ,
  28 , 29 , 30 , 31 , 32 , 1
};

public void ExpPermutation( byte  [] a, byte [] out)
{
  int i;
  Debug("ExpPermutation\n");
  for( i=0 ; i < 48 ; i++ )
    { 
      out[i] = a[ XP[i]-1 ];
    }
  if(DEBUG)
   {
     for(i=0 ; i < 48 ; i++ )
        System.out.println("%x"+out[i]);
     System.out.println("\n");
   }
}

byte[] PC2={
  14 , 17 , 11 , 24 , 1  , 5  , 3  , 28,
  15 , 6  , 21 , 10 , 23 , 19 , 12 , 4 ,
  26 , 8  , 16 , 7  , 27 , 20 , 13 , 2 ,
  41 , 52 , 31 , 37 , 47 , 55 , 30 , 40,
  51 , 45 , 33 , 48 , 44 , 49 , 39 , 56,
  34 , 53 , 46 , 42 , 50 , 36 , 29 , 32
};
 
public void PermutationContraction( byte  [] a, byte [] out)
{
  int i;
  Debug("PermutationContraction\n");
  for( i=0 ; i < 48 ; i++ )
       out[ i ] = a[ PC2[i]-1 ];
  if(DEBUG)
   {
     for(i=0 ; i < 48 ; i++ )
        System.out.println("%x"+out[i]);
     System.out.println("\n");
   }
}

byte[] IIP={
  40 , 8 , 48 , 16 , 56 , 24 , 64 , 32 ,
  39 , 7 , 47 , 15 , 55 , 23 , 63 , 31 ,
  38 , 6 , 46 , 14 , 54 , 22 , 62 , 30 ,
  37 , 5 , 45 , 13 , 53 , 21 , 61 , 29 ,
  36 , 4 , 44 , 12 , 52 , 20 , 60 , 28 ,
  35 , 3 , 43 , 11 , 51 , 19 , 59 , 27 ,
  34 , 2 , 42 , 10 , 50 , 18 , 58 , 26 ,
  33 , 1 , 41 ,  9 , 49 , 17 , 57 , 25
};

public void InvIniPermutation( byte [] a, byte [] out)
{
  int i;
  Debug("InvIniPermutation\n");
   for( i=0 ; i < 64 ; i++ )
     out[ IIP[i]-1 ] = a[i];
  if(DEBUG)
    {
      for( i=0 ; i < 64 ; i++)
        System.out.println("%x"+out[i]);
      System.out.println("\n"); 
    }
}

int[] Rot={
  1 , 1 , 2 , 2 , 2 , 2 , 2 , 2 , 1 , 2 , 2 , 2 , 2 , 2 , 2 , 1 ,0 
};

public void LeftCShift( byte[]  key,int i, byte [] out)
{
  int j;
  Debug("LeftCShift \n");
  switch( Rot[i] ){
  case 1:
    out[0]=key[1];
    out[1]=key[2];
    out[2]=key[3];
    out[3]=key[4];
    out[4]=key[5];
    out[5]=key[6];
    out[6]=key[7];
    out[7]=key[8];
    out[8]=key[9];
    out[9]=key[10];
    out[10]=key[11];
    out[11]=key[12];
    out[12]=key[13];
    out[13]=key[14];
    out[14]=key[15];
    out[15]=key[16];
    out[16]=key[17];
    out[17]=key[18];
    out[18]=key[19];    
    out[19]=key[20];
    out[20]=key[21];
    out[21]=key[22];
    out[22]=key[23];
    out[23]=key[24];
    out[24]=key[25];    
    out[25]=key[26];
    out[26]=key[27];
    out[27]=key[0];

    out[28]=key[29];
    out[29]=key[30];
    out[30]=key[31];
    out[31]=key[32];
    out[32]=key[33];
    out[33]=key[34];
    out[34]=key[35];
    out[35]=key[36];
    out[36]=key[37];
    out[37]=key[38];
    out[38]=key[39];
    out[39]=key[40];
    out[40]=key[41];
    out[41]=key[42];
    out[42]=key[43];
    out[43]=key[44];
    out[44]=key[45];
    out[45]=key[46];    
    out[46]=key[47];
    out[47]=key[48];
    out[48]=key[49];
    out[49]=key[50];
    out[50]=key[51];
    out[51]=key[52];
    out[52]=key[53];
    out[53]=key[54];
    out[54]=key[55];
    out[55]=key[28];
  break;
  case 2:
    out[0]=key[2];
    out[1]=key[3];
    out[2]=key[4];
    out[3]=key[5];
    out[4]=key[6];
    out[5]=key[7];
    out[6]=key[8];
    out[7]=key[9];
    out[8]=key[10];
    out[9]=key[11];
    out[10]=key[12];
    out[11]=key[13];
    out[12]=key[14];
    out[13]=key[15];
    out[14]=key[16];
    out[15]=key[17];
    out[16]=key[18];
    out[17]=key[19];
    out[18]=key[20];    
    out[19]=key[21];
    out[20]=key[22];
    out[21]=key[23];
    out[22]=key[24];
    out[23]=key[25];
    out[24]=key[26];    
    out[25]=key[27];
    out[26]=key[0];
    out[27]=key[1];

    out[28]=key[30];
    out[29]=key[31];
    out[30]=key[32];
    out[31]=key[33];
    out[32]=key[34];
    out[33]=key[35];
    out[34]=key[36];
    out[35]=key[37];
    out[36]=key[38];
    out[37]=key[39];
    out[38]=key[40];
    out[39]=key[41];
    out[40]=key[42];
    out[41]=key[43];
    out[42]=key[44];
    out[43]=key[45];
    out[44]=key[46];
    out[45]=key[47];    
    out[46]=key[48];
    out[47]=key[49];
    out[48]=key[50];
    out[49]=key[51];
    out[50]=key[52];
    out[51]=key[53];
    out[52]=key[54];
    out[53]=key[55];
    out[54]=key[28];
    out[55]=key[29];    
  break;
  }
  for(j=0 ; j< 56 ; j++ )
    key[j]=out[j];
  if(DEBUG)
    { 
      System.out.println("Rotated Key ");
      for( j = 0 ; j < 56 ; j++ )
          System.out.println("%x"+out[j]);
      System.out.println("\n");
    }
}

public void RightCShift( byte [] key,int i, byte [] out)
{
  int j;
  Debug("RightCShift \n");
  switch( Rot[i] ){
  case 0:
      Copy(key,out,56);
    break;
  case 1:
    out[0]=key[27];
    out[1]=key[0];
    out[2]=key[1];
    out[3]=key[2];
    out[4]=key[3];
    out[5]=key[4];
    out[6]=key[5];
    out[7]=key[6];
    out[8]=key[7];
    out[9]=key[8];
    out[10]=key[9];
    out[11]=key[10];
    out[12]=key[11];
    out[13]=key[12];
    out[14]=key[13];
    out[15]=key[14];
    out[16]=key[15];
    out[17]=key[16];
    out[18]=key[17];    
    out[19]=key[18];
    out[20]=key[19];
    out[21]=key[20];
    out[22]=key[21];
    out[23]=key[22];
    out[24]=key[23];    
    out[25]=key[24];
    out[26]=key[25];
    out[27]=key[26];

    out[28]=key[55];
    out[29]=key[28];
    out[30]=key[29];
    out[31]=key[30];
    out[32]=key[31];
    out[33]=key[32];
    out[34]=key[33];
    out[35]=key[34];
    out[36]=key[35];
    out[37]=key[36];
    out[38]=key[37];
    out[39]=key[38];
    out[40]=key[39];
    out[41]=key[40];
    out[42]=key[41];
    out[43]=key[42];
    out[44]=key[43];
    out[45]=key[44];    
    out[46]=key[45];
    out[47]=key[46];
    out[48]=key[47];
    out[49]=key[48];
    out[50]=key[49];
    out[51]=key[50];
    out[52]=key[51];
    out[53]=key[52];
    out[54]=key[53];
    out[55]=key[54];
  break;
  case 2:
    out[0]=key[26];
    out[1]=key[27];
    out[2]=key[0];
    out[3]=key[1];
    out[4]=key[2];
    out[5]=key[3];
    out[6]=key[4];
    out[7]=key[5];
    out[8]=key[6];
    out[9]=key[7];
    out[10]=key[8];
    out[11]=key[9];
    out[12]=key[10];
    out[13]=key[11];
    out[14]=key[12];
    out[15]=key[13];
    out[16]=key[14];
    out[17]=key[15];
    out[18]=key[16];    
    out[19]=key[17];
    out[20]=key[18];
    out[21]=key[19];
    out[22]=key[20];
    out[23]=key[21];
    out[24]=key[22];    
    out[25]=key[23];
    out[26]=key[24];
    out[27]=key[25];

    out[28]=key[54];
    out[29]=key[55];
    out[30]=key[28];
    out[31]=key[29];
    out[32]=key[30];
    out[33]=key[31];
    out[34]=key[32];
    out[35]=key[33];
    out[36]=key[34];
    out[37]=key[35];
    out[38]=key[36];
    out[39]=key[37];
    out[40]=key[38];
    out[41]=key[39];
    out[42]=key[40];
    out[43]=key[41];
    out[44]=key[42];
    out[45]=key[43];    
    out[46]=key[44];
    out[47]=key[45];
    out[48]=key[46];
    out[49]=key[47];
    out[50]=key[48];
    out[51]=key[49];
    out[52]=key[50];
    out[53]=key[51];
    out[54]=key[52];
    out[55]=key[53];    
  break;
  }
  for(j=0 ; j< 56 ; j++ )
    key[j]=out[j];
  if(DEBUG)
    { 
      System.out.println("Rotated Key ");
      for( j = 0 ; j < 56 ; j++ )
          System.out.println("%x"+out[j]);
      System.out.println("\n");
    }
}

public void GetLeft( byte  []a, byte [] out)
{
  int i;
  Debug("GetLeft Data\n");
  for( i = 0 ; i < 32 ; i++)
    out[i] = a[i];
  if(DEBUG)
    {
      for( i=0 ; i < 32 ; i++)
        System.out.println("%x"+out[i]);
      System.out.println("\n"); 
    }
}

public void GetRight( byte [] a, byte  [] out)
{
  int i;
  Debug("GetRight Data\n");
  for( i = 0 ; i < 32 ; i++)
    out[i] = a[32+i];
  if(DEBUG)
    {
      for( i=0 ; i < 32 ; i++)
        System.out.println("%x"+out[i]);
      System.out.println("\n"); 
    }
}

public void Xor( byte  []a, byte [] b,int num, byte []  out)
{
  int i;
  Debug("Xor\n");
  for( i=0 ; i < num ; i++ )
   {
    int tmp = (int) a[i]^b[i];
    out[i] =(byte)tmp;
   }
  if(DEBUG)
   {
     for(i=0 ; i < num ; i++ )
        System.out.println("%x"+out[i]);
     System.out.println("\n");
   }
}

byte[][][] S={
  //S1 
  {
  {14 , 4  , 13 , 1 , 2  , 15 , 11 , 8  , 3  , 10 , 6  , 12 , 5  , 9  , 0 , 7},
  {0  , 15 , 7  , 4 , 14 , 2  , 13 , 1  , 10 , 6  , 12 , 11 , 9  , 5  , 3 , 8},
  {4  , 1  , 14 , 8 , 13 , 6  , 2  , 11 , 15 , 12 , 9  , 7  , 3  , 10 , 5 , 0},
  {15 , 12 , 8  , 2 , 4  , 9  , 1  , 7  , 5  , 11 , 3  , 14 , 10 , 0  , 6 , 13}
  },
  //S2
  {
  {15 , 1  , 8  , 14 , 6  , 11 , 3  , 4  , 9  , 7 , 2  , 13 , 12 , 0 , 5  , 10},
  {3  , 13 , 4  , 7  , 15 , 2  , 8  , 14 , 12 , 0 , 1  , 10 , 6  , 9 , 11 , 5},
  {0  , 14 , 7  , 11 , 10 , 4  , 13 , 1  , 5  , 8 , 12 , 6  , 9  , 3 , 2  , 15},
  {13 , 8  , 10 , 1  , 3  , 15 , 4  , 2  , 11 , 6 , 7  , 12 , 0  , 5 , 14 , 9}
  },
  //S3
  {
  {10 , 0  , 9  , 14 , 6 , 3  , 15 , 5  , 1  , 13 , 12 , 7  , 11 , 4  , 2  ,8},
  {13 , 7  , 0  , 9  , 3 , 4  , 6  , 10 , 2  , 8  , 5  , 14 , 12 , 11 , 15 ,1},
  {13 , 6  , 4  , 9  , 8 , 15 , 3  , 0  , 11 , 1  , 2  , 12 , 5  , 10 , 14 ,7},
  {1  , 10 , 13 , 0  , 6 , 9  , 8  , 7  , 4  , 15 , 14 , 3  , 11 , 5  , 2  ,12}
  },
  //S4
  {
  {7  , 13 , 14 , 3  , 0  , 6  , 9  , 10 , 1  , 2  , 8  , 5  , 11 , 12 , 4  ,15},
  {13 , 8  , 11 , 5  , 6  , 15 , 0  , 3  , 4  , 7  , 2  , 12 , 1  , 10 , 14 ,9},
  {10 , 6  , 9  , 0  , 12 , 11 , 7  , 13 , 15 , 1  , 3  , 14 , 5  , 2  , 8  ,4},
  {3  , 15 , 0  , 6  , 10 , 1  , 13 , 8  , 9  , 4  , 5  , 11 , 12 , 7  , 2  ,14}
  },
  //S5
  {
  {2  , 12 , 4  , 1  , 7  , 10 , 11 , 6  , 8  , 5  , 3  , 15 , 13 , 0 , 14 , 9},
  {14 , 11 , 2  , 12 , 4  , 7  , 13 , 1  , 5  , 0  , 15 , 10 , 3  , 9 , 8  , 6},
  {4  , 2  , 1  , 11 , 10 , 13 , 7  , 8  , 15 , 9  , 12 , 5  , 6  , 3 , 0  , 14},
  {11 , 8  , 12 , 7  , 1  , 14 , 2  , 13 , 6  , 15 , 0  , 9  , 10 , 4 , 5  , 3}
  },
  //S6
  {
  {12 , 1  , 10 , 15 , 9 , 2  , 6  , 8  , 0  , 13 , 3  , 4  , 14 , 7  , 5  , 11}, 
  {10 , 15 , 4  , 2  , 7 , 12 , 9  , 5  , 6  , 1  , 13 , 14 , 0  , 11 , 3  , 8},
  {9  , 14 , 15 , 5  , 2 , 8  , 12 , 3  , 7  , 0  , 4  , 10 , 1  , 13 , 11 , 6},
  {4  , 3  , 2  , 12 , 9 , 5  , 15 , 10 , 11 , 14 , 1  , 7  , 6  , 0  , 8  , 13}
  },
  //S7
  {
  {4  , 11 , 2  , 14 , 15 , 0 , 8  , 13 , 3  , 12 , 9 , 7  , 5  , 10 , 6 , 1},
  {13 , 0  , 11 , 7  , 4  , 9 , 1  , 10 , 14 , 3  , 5 , 12 , 2  , 15 , 8 , 6},
  {1  , 4  , 11 , 13 , 12 , 3 , 7  , 14 , 10 , 15 , 6 , 8  , 0  , 5  , 9 , 2},
  {6  , 11 , 13 , 8  , 1  , 4 , 10 , 7  , 9  , 5  , 0 , 15 , 14 , 2  , 3 , 12}
  },
  //S8
  {
  {13 , 2  , 8  , 4 , 6  , 15 , 11 , 1  , 10 , 9  , 3  , 14 , 5  , 0  , 12 , 7},
  {1  , 15 , 13 , 8 , 10 , 3  , 7  , 4  , 12 , 5  , 6  , 11 , 0  , 14 , 9  , 2},
  {7  , 11 , 4  , 1 , 9  , 12 , 14 , 2  , 0  , 6  , 10 , 13 , 15 , 3  , 5  , 8},
  {2  , 1  , 14 , 7 , 4  , 10 , 8  , 13 , 15 , 12 , 9  , 0  , 3  , 5  , 6  , 11}
  }
};

byte GetBin( byte a, byte b)
{
      if((a==0) && (b==0))
	return 0;
      else
      if((a==0) && (b==1))
        return 1;
      else
      if((a==1) && (b==0))
        return 2; 
      else
      if((a==1) && (b==1))
        return 3;
}

public void SubstitutionBox( byte [] a,byte []out)
{
   int x,w;
   byte[] Y=new byte[8];
   byte[] Z=new byte[8];
   byte[] R=new byte[4];
  Debug("S-Box\n");
  for( x=0,w=0 ; x < 43 ; x=x+6,w++ )
    {
      
      int zzz=((( a[x+1] << 3 ) | a[x+2] << 2 )| a[x+3] << 1 ) | a[x+4];
      Y[w]=(byte)zzz;
      Z[w]=GetBin(a[x],a[x+5]);
      if(DEBUG)
	{
          System.out.println("%d %x %x S %x"+w+Z[w]+Y[w]+S[w][Z[w]][Y[w]]);
          System.out.println("\n");
        }
    }
  
  int aa = (int)( S[0][Z[0]][Y[0]] <<4 ) | S[1][Z[1]][Y[1]];
  R[0]=(byte)aa;  
  
  int bb = (int)( S[2][Z[2]][Y[2]] <<4 ) | S[3][Z[3]][Y[3]];
  R[1]=(byte)bb; 
  int cc = (int)( S[4][Z[4]][Y[4]] <<4 ) | S[5][Z[5]][Y[5]];
  R[2]=(byte)cc; 
  int dd = (int)( S[6][Z[6]][Y[6]] <<4 ) | S[7][Z[7]][Y[7]];
  R[3]=(byte)dd;
  if(DEBUG)
    System.out.println("%02x %02x %02x %02x\n"+R[0]+R[1]+R[2]+R[3]);
  tobinary(R,4,out);
}

byte[] P={
  16 , 7  , 20 , 21 , 29 , 12 , 28 , 17 ,
  1  , 15 , 23 , 26 , 5  , 18 , 31 , 10 ,
  2  , 8  , 24 , 14 , 32 , 27 , 3  , 9  ,
  19 , 13 , 30 , 6  , 22 , 11 , 4  , 25 ,
};

public void Permutation( byte  [] a, byte [] out)
{
  int i;
  Debug("Permutation\n"); 
  for( i = 0 ; i < 32 ; i++ )
     out[ P[i+1]-1 ] = a[i];
  if(DEBUG)
   {
     for(i=0 ; i < 32 ; i++ )
        System.out.println("%x"+out[i]);
     System.out.println("\n");
   }
}

public void ConvData( byte []a, byte []out)
{
  int i,k,j;
  Debug("Conv Data\n");
  for ( i=0 , k=0; i < 8 ; i++ , k=k+8)
    {  
      out[i]=0;
       for( j=0 ; j < 8 ; j++)
	 {
           int tmp = out[i] | a[k+j];
             out[i]=(byte)tmp;
	   if(j!=7)
             {
               int tmp2= out[i];
                   tmp2= tmp2<<1;
                   out[i]=(byte)tmp2;
              //out[i] = out[i]<<1;
             }
         }
    }
  if(DEBUG)
    {
      for( i=0 ; i < 8 ; i++ )
	System.out.println("%x "+out[i]);
      System.out.println("\n");
    }
}

int Encryption( byte[] Buffer,  byte[] KEY, byte []out, int mode)
{  
   byte[] K=new byte[64];
   byte[] Ktmp=new byte[64];
   byte[] key=new byte[64];
   byte[] BLOCK=new byte[64];
   byte[] Data=new byte[64];
   byte[] Left=new byte[32];
   byte[] Right=new byte[32];
   byte[] RightE=new byte[48];
   byte[] Temp=new byte[32]; 
   long W=size;
   int  i;
   byte[] ISBox=new byte[48];
   byte[] OSBox=new byte[48];
   byte[] ROut=new byte[48];
   G=0;
  do{     
       //tobinary(&Buffer[G],8,BLOCK);        //[OK]
       tobinary(Buffer[G],8,BLOCK);        //[OK]
       IniPermutation(BLOCK,Data);          //[OK]
       Permutation1(KEY,key);               //[OK]
       GetLeft(Data,Left);                  //[OK]
       GetRight(Data,Right);                //[OK]
       for( i=0 ; i < 16 ; i++ )
          {
            if(DEBUG)
               System.out.println("Round [%2d ]\n"+i);
            Copy(Right,Temp,32);            //[OK]
	    ExpPermutation(Right,RightE);   //[OK]
            if(mode==0)
              LeftCShift(key,i,K);          //[OK]
            else
              RightCShift(key,16-i,K);      //[OK]
	    PermutationContraction(K,Ktmp); //[OK]
            Xor(RightE,Ktmp,48,ISBox);      //[OK]
            SubstitutionBox(ISBox,OSBox);   //[OK]
            Permutation(OSBox,ROut);        //[OK]
            Xor(Left,ROut,32,Right);        //[OK]
            Copy(Temp,Left,32);             //[OK]
          }
       //Copy(Right,&Data[0],32);             //[OK]
       Copy(Right,Data[0],32);             //[OK]
       //Copy(Left,&Data[32],32);             //[OK]
       Copy(Left,Data[32],32);             //[OK]
       InvIniPermutation(Data,BLOCK);       //[OK]  
       //ConvData(BLOCK,&out[G]);             //[OK]
       ConvData(BLOCK,out[G]);             //[OK]
       W=W-8;
       if(DEBUG)
         System.out.println("G=%d\n"+G);
       G=G+8; 
   }while( W > 0 );
  return G;
}

public void writefile(String file, byte[]  Buffer,int si)
{
  int i;
  Debug("writefile\n");
  if(DEBUG)
    {
      for( i=0 ; i< si ; i++ )
        System.out.println("%x "+Buffer[i]);
      System.out.println("\n");
    }
  //if((i=write(ofile,Buffer,si))==-1)
    {
      System.out.println("ERROR: cannot save file > %s\n"+file);
      System.exit(1);
    }
}

public void Info()
{
  System.out.println("Data Encryption Standard\n");
  System.out.println("3 DES - Triple Key\n");
  System.out.println("Cipher Block Chaining Mode\n");
  System.out.println("des <options> <input file> <output file> <keys file>\n");
  System.out.println("options:\n");
  System.out.println("0  -->e: encryption\n");
  System.out.println("1  -->d: decryption\n");
}
public void clean( byte []k1, byte []k2,  byte []k3, byte []in, byte []o)
{
  int i;
  for(i=0 ; i < KEY_SIZE ; i++ )
    {
      k1[i]=k2[i]=k3[i]=0;
    }
  for(i=0 ; i < BIG_BLOCK ; i++)
    {
      in[i]=0;
      o[i]=0;
    }
}
// Main Procedure 
public void doall(String [] args)
{
  int z,Q;
    byte[] INPUT=new byte[1000000];
    byte[] OUT  =new byte[1000000];
    byte[] O2   =new byte[1000000];
    byte[] O3   =new byte[1000000];
   if(args.length!=7)
     {
       System.out.println("Wrong number of parameters\n");
       Info();
       System.exit(1);
     }
  
   int option=0;
   if(args[0]=="e")
    { option = 0;}
    else
    { option = 1;}
   switch(option)
     {
        case 0:
	  System.out.println("Encryption Mode\n");
	  //openfiles(args[2],args[3],args[4],args[5],args[6]);
          //readfile(kfile,KEY_SIZE,K1);   
          //readfile(kfile2,KEY_SIZE,K2);
          //readfile(kfile3,KEY_SIZE,K3);
          transform(K1); transform(K2); transform(K3);
          do{ 
              //z = readfile(ifile,BIG_BLOCK,INPUT);
              if( z > 0 )
                {
                  Q = Encryption(INPUT,K1,OUT,0);   
                  //Q = Encryption(OUT  ,K2,O2,1);
                  //Q = Encryption(O2   ,K3,O3,0);
                  //writefile(args[3],OUT,Q);
                }
	  }while( z > 0 );
          //closefiles(args[2],args[3],args[4],args[5],args[6]);
          clean(K1,K2,K3,INPUT,OUT);
          break;
        case 1:
          System.out.println("Decryption Mode\n");
          //openfiles(args[2],args[3],args[4],args[5],args[6]);
          //readfile(kfile,KEY_SIZE,K1);   
          //readfile(kfile2,KEY_SIZE,K2);
          //readfile(kfile3,KEY_SIZE,K3);
          transform(K1); transform(K2); transform(K3); 
          do{
              //z = readfile(ifile,BIG_BLOCK,INPUT);
              if( z > 0)
		{
                  Q = Encryption(INPUT,K3,OUT,1);
                  //Q = Encryption(OUT  ,K2,O2,0);
                  //Q = Encryption(O2   ,K1,O3,1);   
                  //writefile(args[3],OUT,Q);
                }
          }while( z > 0 );
          //this.closefiles(args[2],args[3],args[4],args[5],args[6]);
          clean(K1,K2,K3,INPUT,OUT);
          break;
        case 'h':
          Info();
          break; 
     }   
   //return 1;
}


    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
      des a = new des();
          a.doall(args);
   }

    
}
