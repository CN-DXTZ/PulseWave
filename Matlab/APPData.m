clc; clear;close all;

a = textread('app3.txt');
a = a(10:end);

plot(-a)
